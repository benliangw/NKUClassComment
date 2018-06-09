var util = require('../../utils/MD5.js');   
Page({
  loginButtonClick: function (e) {
    var _stuID = e.detail.value.stuId
    var _password = e.detail.value.password
    if(_stuID.length==0|| _password.length==0){
      wx.showToast({
        title: '用户名或密码不能为空',
        icon:'none'
      })
    }else{
      wx.request({
        url: 'https://xcx.benliang.wang/xcx/LoginServlet',
        method: 'POST',
        data:{
          userid: _stuID,
          password: util.hexMD5(_password)
        },
        success: function(res) {
          if(res.statusCode!=200){
            wx.showToast({
              title: '网络异常，请稍后重试',
              icon:'none'
            })
          }else{
            if(res.data.status==200){
              wx.showLoading({
                title: '加载中',
              })
              getApp().globalData.isLogin = true
              getApp().globalData.userInfo = res.data.user
              wx.switchTab({
                url: '/pages/catalog/catalog'  
              });
              wx.hideLoading()
            }else if(res.data.status==404){
              wx.showToast({
                title: '账号不存在',
                icon:'none'
              })
            }else{
              wx.showToast({
                title: '密码错误',
                icon:'none'
              })
            }
          }
        }
      })
    }
  }
  
})