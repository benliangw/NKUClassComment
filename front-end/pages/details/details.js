// pages/details/details.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    comment:{},
    classInfo:{},
    courseInfo:{},
    aveScore:0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    var that = this
    var time = require('../../utils/util.js')
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetCommentByIDServlet',
      method:'POST',
      data:{
        commentid:options.commentId
      },
      success: function(res){
        console.log(res.data)
        var _tmp = new Date(
          res.data.comment.comment_time.year+1900,
          res.data.comment.comment_time.month,
          res.data.comment.comment_time.date,
          res.data.comment.comment_time.hours,
          res.data.comment.comment_time.minutes,
        )
        var _time = time.formatTime(_tmp)
        res.data.comment['format_time'] = _time
        that.setData({
          comment:res.data.comment
        })
      }
      
    })
    //获取课程评分
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetClassScoreServlet',
      method: 'POST',
      data: {
        classid: options.classid

      },
      success: function (res) {
        if (res.statusCode != 200) {
          wx.showToast({
            title: '网路错误',
            icon:"none"
          })
        }else if(res.data.status==200){
          that.setData({
            aveScore: res.data.score
          })
        }
      }
    })
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetClassMoreByIDServlet',
      method: 'POST',
      data: {
        classid: options.classid
      },
      success: function (res) {
        if(res.statusCode!=200){
          wx.showToast({
            title: '网络错误',
            icon:'none'
          })
        }else{
          if(res.data.status==200){
            that.setData({
              classInfo:res.data.class
            })
          }
        }
      }
    })

    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetClassBasicByIDServlet',
      method: 'POST',
      data: {
        classid: options.classid
      },
      success: function (res) {
        if (res.statusCode != 200) {
          wx.showToast({
            title: '网络错误',
            icon: "none"
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
          icon: 'none'
        })
      }
    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})