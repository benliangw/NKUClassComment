Page({
  data: {
    title:'我的回复',
    cursor:0,
    hideloading:true,
    current_id:'',
    comment_list: [],
    showModalStatus:false,
    animationData:{}
  },
  onPullDownRefresh:function(){
    console.log(1)
  },
  //TODO 报错
  onReady: function () {
    wx.setNavigationBarTitle({
      title: this.data.title
    })
  },
  refresh:function(cursor){
    var time = require('../../utils/util.js');
    var that = this
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetCommentsByUserServlet',
      method: 'POST',
      data: {
        userid: getApp().globalData.userInfo.userId,
        cursor: cursor
      },
      success: function (res) {
        console.log(res)
        if (res.statusCode != 200) {
          wx.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          })
        } else {
          if (res.data.status == 200) {
            var comment = res.data.comment
            for (var i = 0; i < comment.length; i++) {
              var temp = new Date(
                comment[i].comment_time.year + 1900,
                comment[i].comment_time.month,
                comment[i].comment_time.date,
                comment[i].comment_time.hours,
                comment[i].comment_time.minutes
              )
              comment[i]['format_time'] = time.formatTime(temp)
            }
            that.setData({
              comment_list: res.data.comment
            })
          } else if (res.data.status == 100) {
            that.setData({
              comment_list: []
            })
          } else {
            wx.showToast({
              title: '网络异常，请稍后重试',
              icon: 'none'
            })
          }
        }
      }
    })
  },
  onLoad: function () {
    this.refresh(0)
  },
  setAnnoy:function(e){
    var ids = e.currentTarget.id
    var contents,scores
    for (var i = 0; i < this.data.comment_list.length;i++){
      if(ids == this.data.comment_list[i].comment_id){
        scores = this.data.comment_list[i].comment_score
        contents = this.data.comment_list[i].comment_content
        break
      }
    }
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/ModifyCommentServlet',
      Method:'POST',
      data:{
        commentid:ids,
        content:contents,
        score:scores,
        isannoy: e.detail.value
      },
      success: function(res){
        console.log(res)
        if(res.data.status==200){
          wx.showToast({
            title:'修改成功',
            icon:'none'
          })
        }else{
          wx.showToast({
            title: '修改失败',
            icon: 'none'
          })
        }
      }
    })
  },
  onReachBottom:function(e){
    this.setData({
      hideloading:false,
    })
    var that = this
    var time = require('../../utils/util.js');
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetCommentsByUserServlet',
      method: 'POST',
      data: {
        userid: getApp().globalData.userInfo.userId,
        cursor: this.data.cursor + 10
      },
      success: function (res) {
        if (res.statusCode != 200) {
          wx.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          })
        } else {
          if (res.data.status == 200) {
            var comment = res.data.comment
            for (var i = 0; i < comment.length; i++) {
              var temp = new Date(
                comment[i].comment_time.year + 1900,
                comment[i].comment_time.month,
                comment[i].comment_time.date,
                comment[i].comment_time.hours,
                comment[i].comment_time.minutes
              )
              comment[i]['format_time'] = time.formatTime(temp)
            }
            that.setData({
              comment_list: that.data.comment_list.concat(res.data.comment),
              cursor:that.data.cursor+10,
            })
          } else if (res.data.status == 100) {
            wx.showToast({
              title:'无更多数据',
              icon:'none'
            })
          } else {
            wx.showToast({
              title: '网络异常，请稍后重试',
              icon: 'none'
            })
          }
        }
      }
    })
    that.setData({
      hideloading: true,
    })
  },
  onPullDownRefresh: function (e) {
    this.setData({
      hideloading: false,
    })

    if(this.data.cursor<10){
      this.setData({
        hideloading: true
      })
      return
    }
    var that = this
    var time = require('../../utils/util.js');
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetCommentsByUserServlet',
      method: 'POST',
      data: {
        userid: getApp().globalData.userInfo.userId,
        cursor: this.data.cursor -10
      },
      success: function (res) {
        if (res.statusCode != 200) {
          wx.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          })
        } else {
          if (res.data.status == 200) {
            var comment = res.data.comment
            for (var i = 0; i < comment.length; i++) {
              var temp = new Date(
                comment[i].comment_time.year + 1900,
                comment[i].comment_time.month,
                comment[i].comment_time.date,
                comment[i].comment_time.hours,
                comment[i].comment_time.minutes
              )
              comment[i]['format_time'] = time.formatTime(temp)
            }
            that.setData({
              comment_list: res.data.comment,
              cursor:that.data.cursor-10,
            })
          } else if (res.data.status == 100) {
            wx.showToast({
              title: '无更多数据',
              icon: 'none'
            })
          } else {
            wx.showToast({
              title: '网络异常，请稍后重试',
              icon: 'none'
            })
          }
        }
      }
    })
    that.setData({
      hideloading: true,
    })
  },
  menuOut: function(e){
    this.setData({
      showModalStatus:true,
      current_id:e.currentTarget.id
    })
  },
  look: function(e){
    for (var i = 0; i < this.data.comment_list.length; i++) {
      if (this.data.comment_list[i].comment_id == this.data.current_id) {
        wx.navigateTo({
          url: '/pages/course/course?id=' + this.data.comment_list[i].comment_class_id + '&showmodal=' + false,
        })
        this.setData({
          showModalStatus: false
        })
        break;
      }
    }
  },
  modify: function(){
    for (var i = 0; i < this.data.comment_list.length;i++){
      if (this.data.comment_list[i].comment_id == this.data.current_id){
        wx.navigateTo({
          url: '/pages/course/course?id=' + this.data.comment_list[i].comment_class_id + '&showmodal=' + true,
        })
        this.setData({
          showModalStatus:false
        })
        break;
      }
    }
  },
  deletee: function(){
    console.log('delete')
    var that = this
    var time = require("../../utils/util.js")
    wx.showModal({
      content: '是否确认删除',
      success: function (res) {
        if (res.confirm) {
          wx.request({
            url: 'https://xcx.benliang.wang/xcx/DeleteCommentServlet',
            method:'POST',
            data:{
              commentid: that.data.current_id
            },
            success: function(e){
              if(e.statusCode==200){
                if(e.data.status==200){
                  wx.showToast({
                    title:'删除成功',
                    icon:'none'
                  })
                  wx.request({
                    url: 'https://xcx.benliang.wang/xcx/GetCommentsByUserServlet',
                    method: 'POST',
                    data: {
                      userid: getApp().globalData.userInfo.userId,
                      cursor: that.data.cursor
                    },
                    success: function (res) {
                      console.log(res)
                      if (res.statusCode != 200) {
                        wx.showToast({
                          title: '网络异常，请稍后重试',
                          icon: 'none'
                        })
                      } else {
                        if (res.data.status == 200) {
                          var comment = res.data.comment
                          for (var i = 0; i < comment.length; i++) {
                            var temp = new Date(
                              comment[i].comment_time.year + 1900,
                              comment[i].comment_time.month,
                              comment[i].comment_time.date,
                              comment[i].comment_time.hours,
                              comment[i].comment_time.minutes
                            )
                            comment[i]['format_time'] = time.formatTime(temp)
                          }
                          that.setData({
                            comment_list: res.data.comment
                          })
                        } else if (res.data.status == 100) {
                          that.setData({
                            comment_list: []
                          })
                        } else {
                          wx.showToast({
                            title: '网络异常，请稍后重试',
                            icon: 'none'
                          })
                        }
                      }
                    }
                  })
                }else{
                  wx.showToast({
                    title: '删除失败',
                    icon: 'none'
                  })
                }
              }else{
                wx.showToast({
                  title: '网络异常，请稍后重试',
                  icon: 'none'
                })
              }
              that.setData({
                showModalStatus: false
              })
            }
          })
        }
      }
    })
    console.log(this.data.current_id)
  },
  cancel: function(){
    this.setData({
      showModalStatus:false,
      current_id:''
    })
    console.log(this.data.current_id)
  },
  showModal: function () {
    // 显示遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },
  hideModal: function () {
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false,
        current_id:''
      })
    }.bind(this), 200)
  }
})
