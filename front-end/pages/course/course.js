var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    courseInfo: {},
    userInfo:{},
    thisScore: 0,
    showModalStatus: false,
    animationData: {},
    hasReply:false,
    thisComment:'',
    aveScore:0,
    currentCursor:0,
    thisAnnoy:false,
    comment:'',
    commentList:[
    ],
    courseDetail:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  
  onLoad: function (option) {
    var that = this
    console.log(option)
    if(option.showmodal=="true"){
      this.setData({
        showModalStatus:true
      })
    }
    that.refresh(option.id)
  },
  refresh: function (id) {
    var that = this
    var time = require('../../utils/util.js')
    wx.showLoading({
      title: '加载数据中',
    })
    var _id = id
    this.setData({
      userInfo: getApp().globalData.userInfo
    })

    //获取课程基本信息
    
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetClassBasicByIDServlet',
      method: 'POST',
      data: {
        classid: _id
      },
      success: function (res) {
        if (res.data.status == 100) {
          wx.showToast({
            title: '无相应课程',
            icon:"none"
          })
        } else if (res.data.status == 200) {
          that.setData({
            courseInfo: res.data.class
          })
        }
      },
      fail: function (res) {
        wx.showToast({
          title: '获取信息失败',
          icon:'none'
        })
      }
    })
    //获取课程评分
    wx.request({

      url: 'https://xcx.benliang.wang/xcx/GetClassScoreServlet',
      method: 'POST',
      data: {
        classid: _id

      },
      success: function (res) {
        if (res.data.status == 200) {
          that.setData({
            aveScore: res.data.score
          })
        }
      }
    })
    //获取详细信息
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetClassMoreByIDServlet',
      method:'POST',
      data:{
        classid:_id
      },
      success:function(res){
        if(res.data.status==200){
          that.setData({
            courseDetail:res.data.class
          })
          console.log(res.data)
        }
      }
    })
    //获取课程评论
    wx.request({

      url: 'https://xcx.benliang.wang/xcx/GetCommentsByClassServlet',
      method:'POST',
      data:{
        classid:_id,
        cursor:that.data.currentCursor

      },
      success: function (res) {
        if (res.data.status == 100) {
          wx.showToast({
            title: '无评论',
            icon:"none"
          })
          that.setData({
            currentCursor: 0,
            commentList: []
          })
        } else if (res.data.status == 200) {
          var _comment = res.data.comment
          for (var i = 0; i < _comment.length; i++) {
            var _tmp = new Date(
              _comment[i].comment_time.year + 1900,
              _comment[i].comment_time.month,
              _comment[i].comment_time.date,
              _comment[i].comment_time.hours,
              _comment[i].comment_time.minutes
            )
            _comment[i]['formatTime'] = time.formatTime(_tmp)
          }
          that.setData({
            commentList: _comment
          })
        }
        wx.hideLoading()
      },
      fail: function (res) {
        wx.hideLoading()
      }
    })
    //获取评论
    wx.request({

      url: 'https://xcx.benliang.wang/xcx/GetCommentByClassUserServlet',
      method: 'POST',
      data: {
        classid: _id,
        userid: that.data.userInfo.userId

      },
      success: function (res) {
        if (res.data.status == 200) {
          that.setData({
            hasReply: true,
            comment: res.data.comment,
            thisScore:res.data.comment.comment_score,
            thisComment:res.data.comment.comment_content,
            thisAnnoy:res.data.comment.comment_annoy
          })
        } else {
          that.setData({
            hasReply: false
          })
        }
      }
    })
    console.log(that.data)
  },

  setAnnoy: function(e){
    this.setData({
      thisAnnoy:e.detail.value
    })
  },



  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    var that = this
    var time = require('../../utils/util.js')
    var _id = this.data.courseInfo.class_id
    if (this.data.currentCursor - 10 < 0) {
      wx.showToast({
        title: '无更多数据',
        icon: 'none'
      })
      return
    }
    console.log("pullDownRefresh")
    wx.showLoading({
      title: '数据加载中',
    })
    this.setData({
      currentCursor: this.data.currentCursor - 10
    })

    var that = this
    console.log("topRefresh:  " + this.data.searchValue)
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetCommentsByClassServlet',
      method: 'POST',
      data: {
        classid: _id,
        cursor: that.data.currentCursor
      },
      success: function (res) {
        console.log(res.data)
        if (res.data.status == 100) {
          wx.showToast({
            title: '无更多数据',
            icon:'none'
          })
        } else if (res.data.status == 200) {
          var _comment = res.data.comment
          for (var i = 0; i < _comment.length; i++) {
            var _tmp = new Date(
              _comment[i].comment_time.year + 1900,
              _comment[i].comment_time.month ,
              _comment[i].comment_time.date,
              _comment[i].comment_time.hours,
              _comment[i].comment_time.minutes
            )
            _comment[i]['formatTime'] = time.formatTime(_tmp)
          }
          that.setData({
            commentList: _comment
          })
        }
        wx.hideLoading()
      },
      fail: function (res) {
        wx.hideLoading()
      }
    }) 
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    var that = this
    var time = require('../../utils/util.js')
    var _id = this.data.courseInfo.class_id
    console.log("bbbb")
    wx.showLoading({
      title: '数据加载中',
    })
    this.setData({
      currentCursor: this.data.currentCursor + 10
    })
    var that = this
    console.log("bottomRefresh:  " + this.data.searchValue)
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetCommentsByClassServlet',
      method: 'POST',
      data: {
        classid: _id,
        cursor: that.data.currentCursor
      },
      success: function (res) {
        console.log(res.data)
        if (res.data.status == 100) {
          wx.showToast({
            title: '无更多数据',
            icon: 'none',
            duration:2000
          })
          that.setData({
            currentCursor: that.data.currentCursor - 10
          })
        } else if (res.data.status == 200) {
          var _comment = res.data.comment
          for (var i = 0; i < _comment.length; i++) {
            var _tmp = new Date(
              _comment[i].comment_time.year + 1900,
              _comment[i].comment_time.month,
              _comment[i].comment_time.date,
              _comment[i].comment_time.hours,
              _comment[i].comment_time.minutes
            )
            _comment[i]['formatTime'] = time.formatTime(_tmp)
          }
          that.setData({
            commentList: that.data.commentList.concat(_comment)
          })
        }
        wx.hideLoading()
      },
      fail: function (res) {
        wx.hideLoading()
      }
    })
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {


  },
  makestar: function (e) {
    this.setData({
      thisScore: this.selectComponent(".click-stars").data.key
    })
  },
  menuOut: function () {
    this.setData({
      showModalStatus: true
    })
  },
  showModal: function () {
    // 显示遮罩层
    var animation = wx.createAnimation({
      duration: 100,
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
    }.bind(this), 50)
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
        showModalStatus: false
      })
    }.bind(this), 200)
  },
  replyInput:function(e) {
    this.setData({
      thisComment:e.detail.value
    })
  },
  replyConfirm: function () {
    var that = this
    if(this.data.thisScore=='0'){
      wx.showToast({
        title:'请先进行评分~',
        icon:'none'
      })
      return
    }else if (this.data.thisComment == '') {
      wx.showToast({
        title: '请输入内容',
        icon: 'none'
      })
      return
    }else {
      if(that.data.hasReply){
        console.log(that.data.comment.comment_id)
        console.log(that.data.thisComment)
        console.log(that.data.thisAnnoy)
        console.log(that.data.thisScore)

        wx.request({
          url: 'https://xcx.benliang.wang/xcx/ModifyCommentServlet',
          method: 'POST',
          data:{
            commentid:that.data.comment.comment_id,
            content:that.data.thisComment,
            isannoy:that.data.thisAnnoy,
            score:that.data.thisScore
          },
          success:function(res){
            console.log(res)
            if(res.statusCode==200){
              if(res.data.status==200){
                wx.showToast({
                  title: '修改成功'
                })
                that.setData({
                  showModalStatus:false
                })
                that.refresh(that.data.courseInfo.class_id)
              }else{
                wx.showToast({
                  title: '修改失败',
                  icon:'none'
                })
              }
            }else{
              wx.showToast({
                title: '网络错误',
                icon:'none'
              })
            }

          }
        })
      }else{
        wx.request({
          url: 'https://xcx.benliang.wang/xcx/AddCommentServlet',
          method: 'POST',
          data: {
            classid: that.data.courseInfo.class_id,
            content: that.data.thisComment,
            isannoy: that.data.thisAnnoy,
            score: that.data.thisScore,
            userid:that.data.userInfo.userId
          },
          success: function (res) {
            if (res.statusCode == 200) {
              if (res.data.status == 200) {
                wx.showToast({
                  title: '回复成功'
                })
                that.setData({
                  showModalStatus: false
                })
                that.refresh(that.data.courseInfo.class_id)
              } else {
                wx.showToast({
                  title: '回复失败',
                  icon: 'none'
                })
              }
            } else {
              wx.showToast({
                title: '网络错误',
                icon: 'none'
              })
            }
          }
        })
      }
    }
  },
  replyCancel:function(e){
    var that = this
    wx.showModal({
      content: '是否确认取消',
      success:function(res){
        if(res.confirm){
          that.setData({
            showModalStatus: false
          })
        }
      }
    })
  },
  replyDelete:function(e){
    var that = this
    wx.showModal({
      content: '是否确认删除',
      success: function (res) {
        if (res.confirm) {
          wx.request({
            url: 'https://xcx.benliang.wang/xcx/DeleteCommentServlet',
            method: 'POST',
            data: {
              commentid: that.data.comment.comment_id
            },
            success: function (e) {
              if (e.statusCode == 200) {
                if (e.data.status == 200) {
                  wx.showToast({
                    title: '删除成功',
                    icon: 'none'
                  })
                  that.refresh(that.data.courseInfo.class_id)
                } else {
                  wx.showToast({
                    title: '删除失败',
                    icon: 'none'
                  })
                }
              } else {
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
  },
  itemClick: function (e) {
    var _class = e.currentTarget.dataset.itemClass
    var _comment_id = e.currentTarget.dataset.itemCommentId
    wx.navigateTo({
      url: '/pages/details/details?commentId=' + _comment_id + '&classid=' + _class,
    })
  },
  clickReplyBtn: function(){
    this.setData({
      showModalStatus:true
    });
  }

})