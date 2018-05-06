var AV = require('leanengine');


AV.Cloud.define('getLikeList', function(request) {
  var userObjectId = request.currentUser.id;
var userFav = request.currentUser.get('userFav');
console.log('userId = ' + userObjectId);
console.log('userFav = ' + userFav);
var user = new AV.Query('UserFav');

var userFavQuery = new AV.Query('UserFav');
return userFavQuery.get(userFav).then(function (thisFav) {
    var userCoef = thisFav.get('interest');
    console.log('userCoef: '+ userCoef);
    var likeList = [];
    var query = new AV.Query('PlaceInfo');
    return query.find().then(function(results) {
        for (var i = 0; i < results.length; i++ ) {
            console.log('itemName: ' + results[i].get('name'));
            var thisItem = {};
            var thisCoef = results[i].get('category');
            var similarity = cosSimilarity(userCoef, thisCoef);
            thisItem.id = results[i].id;
            thisItem.name = results[i].get('name');
            thisItem.imgSrc = results[i].get('imgSrc');
            thisItem.location = results[i].get('location');
            thisItem.similarity = similarity;
            
            likeList.push(thisItem);
            // comsole.log('item: ' + results[i]);
        }
        // console.log(likeList);
        likeList.sort(by("similarity"));
        return likeList;
    });
});

function by(pro) { 
    return function (obj1, obj2) { 
        var val1 = obj1[pro]; 
        var val2 = obj2[pro]; 
        if (val1 < val2 ) { //正序
            return 1; 
        } else if (val1 > val2 ) { 
            return -1; 
        } else { 
            return 0; 
        } 
    };
} 

function cosSimilarity (userCoef, itemCoef) {
    var j, s = 0, p1 = 0, p2 = 0;
    for (j = 0 ; j < 5 ; j++) {
        s += userCoef[j] * itemCoef[j];
        p1 += userCoef[j] * userCoef[j];
        p2 += itemCoef[j] * itemCoef[j];
    }
    var similarity = s / (Math.sqrt(p1) * Math.sqrt(p2));
    console.log('similarity = ' + similarity);
    return similarity;
}
});
AV.Cloud.afterSave('FavoriteLog', function(request) {
  var type = request.object.get('type');
var favId = request.object.get('favId');
var userObjectId = request.object.get('userObjectId');
var fetchClass = type == 1 ? 'PlaceInfo' : 'POIInfo'; //1为景点，2为兴趣点

var userQuery = new AV.Query('_User');
userQuery.get(userObjectId).then(function(userData) {
    var userFav = userData.get('userFav');
    request.object.set('userFav',userFav);
    request.object.save();
    
    //开始计算个人兴趣数组
    var placeQuery = new AV.Query(fetchClass);
    placeQuery.get(favId).then(function(data) {
        var thisCategory = data.get('category');
        console.log('thisCategory:' + thisCategory);
        
        var query = new AV.Query('UserFav');
        query.get(userFav).then(function(userFavData) {
            userFavData.increment('favAmount');
            var userFavList = userFavData.get('favList');
            console.log('userFavList_old:' + userFavList);
            for(j = 0; j < 5; j++) {
                userFavList[j] += thisCategory[j];
            }
            console.log('userFavList_new:' + userFavList);
            userFavData.set('favList',userFavList);
            return userFavData.save();
        });
    });
    
    
});
});
AV.Cloud.afterSave('_User', function(request) {
  var user = request.object.get('username');
var UserFav = AV.Object.extend('UserFav');
var userFav = new UserFav();
userFav.set('userObjectId',request.object.id)
userFav.save().then(function (todo) {
    request.object.set('userFav',userFav.id);
    return request.object.save().then(function(user)  {
        console.log('created UserFav.');
    });
}, function (error) {
    console.error(error);
});
});
AV.Cloud.afterUpdate('UserFav', function(request) {
  var userFavList = request.object.get('favList');
var favAmount = request.object.get('favAmount');

var userInterest = [0,0,0,0,0];
if (favAmount > 0) {
    for(j = 0; j < 5; j++) {
        userInterest[j] += userFavList[j] / favAmount;
    }
}

request.object.set('interest',userInterest);
return request.object.save().then(function(user)  {
    console.log('update interest ok:' + userInterest);
});
});
AV.Cloud.afterDelete('FavoriteLog', function(request) {
  var userFav = request.object.get('userFav');
var type = request.object.get('type');
var favId = request.object.get('favId');
var fetchClass = type == 1 ? 'PlaceInfo' : 'POIInfo'; //1为景点，2为兴趣点

console.log('[delete fav]');
console.log('userFav:'+userFav);

var userQuery = new AV.Query('UserFav');
userQuery.get(userFav).then(function(userFavData) {
    //开始计算个人兴趣数组
    var userFavList = userFavData.get('favList');
    console.log('userFavList_old:' + userFavList);
    
    var placeQuery = new AV.Query(fetchClass);
    placeQuery.get(favId).then(function(data) {
        var thisCategory = data.get('category');
        console.log('thisCategory:' + thisCategory);
        
        userFavData.increment('favAmount', -1);
        for(j = 0; j < 5; j++) {
            userFavList[j] -= thisCategory[j];
        }
        console.log('userFavList_new:' + userFavList);
        userFavData.set('favList',userFavList);
        return userFavData.save();
    });
}, function (error) {
    // 异常处理
    console.log('error:' + error);
});
});
AV.Cloud.define('hello', function(request) {
  return "hello world!"
});