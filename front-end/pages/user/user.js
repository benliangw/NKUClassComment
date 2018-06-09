Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo:{},
    hiddenmodify:true,
    newname:''
  },
  onLoad:function (){
    this.setData({
      userInfo:getApp().globalData.userInfo
    })
  },
  modify:function(){
    this.setData({
      hiddenmodify:false
    })
  },
  input:function(e){
    console.log(e)
    this.setData({
      newname:e.detail.value
    })
  },
  cancel:function(){
    this.setData({
      hiddenmodify:true
    })
  },
  confirm:function(){
    var that = this
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/ChangeUserNameServlet',
      method:'POST',
      data:{
        userid: that.data.userInfo.userId,
        username:that.data.newname
      },
      success:function(res){
        console.log(res)
        if(res.statusCode==200){
          if(res.data.status==200){
            wx.showToast({
              title:'修改成功'
            })
            that.setData({
              hiddenmodify:true
            })
            getApp().globalData.userInfo.canmodify = false
            getApp().globalData.userInfo.username = that.data.newname
            that.setData({
              userInfo: getApp().globalData.userInfo
            })

          }else if (res.data.status==404){
            wx.showToast({
              title: '用户不存在',
              icon:'none'
            })
          }
        }else{
          wx.showToast({
            title: '网络错误',
            icon:"none"
          })
        }
      }
    })

  },
  checkMyReply: function () {
    wx.navigateTo({
      url: '/pages/reply/reply',
    })
  },
  logoutButtonClick: function () {
    wx.redirectTo({
      url: '/pages/login/login',
    })
    getApp().globalData.isLogin = false;
    console.log("LOGOUT"+getApp().globalData.isLogin);
  }
})