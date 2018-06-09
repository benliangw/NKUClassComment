Page({

  /**
   * 页面的初始数据
   */
  data: {
    searchValue: '',

    courseList: [
    ],
    currentCursor: 0,
    preMode: '',
    tips:'    本小程序主要服务于校内学生，用于展示课程信息和选课交流。请各位同学文明交流，共同营造良好的论坛环境。如果有其他问题或建议，可发邮件至：hjdlive@163.com。谢谢大家！'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // for (var i=0;i<50;i++){
    //   wx.request({

    //     url: 'https://xcx.benliang.wang/xcx/AddClassServlet',

    //     data:{
    //       classname: "课程名称"+i,
    //       classteacher: "授课教师名称"+i,
    //       classdept: "开课单位"+i,
    //     }
    //   })
    // }
    var that = this
    wx.showLoading({
      title: '数据加载中',
    })
    wx.request({
      url: 'https://xcx.benliang.wang/xcx/GetAllClassServlet',

      method: 'POST',
      data: {
        cursor: this.data.currentCursor
      },
      success: function (res) {
        if (res.data.status == 100) {
          wx.showToast({
            title: '无相应课程',
            icon:'none'
          })
        } else if (res.data.status == 200) {

          that.setData({
            courseList: res.data.class
          })
        }
        wx.hideLoading()
        console.log(res.data)
      },
      fail: function (res) {
        wx.hideLoading()
      }
    })

  },


  showTips: function () {
    wx.showModal({
      title: '使用须知',
      content: this.data.tips,
      cancelText: '不再提醒',
      cancelColor: "#495A80",
      confirmColor:'#000',
      confirmText: '确认',
      success(res) {
        if (res.cancel) {
          wx.setStorage({
            key: 'notice',
            data: false,
          })
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this
    wx.getStorage({
      key: 'notice',
      success: function (res) {
        console.log(res)
        if (res.data == true) {
          that.showTips()
        }
      },
      fail: function () {
        console.log("get storage failed")
        that.showTips()
      }
    })

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
    if (this.data.currentCursor - 10 < 0) {
      wx.showToast({
        title: '无更多数据',
        icon: 'none',
        duration: 2000

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
    if (this.data.searchValue == '') {
      if (this.data.preMode == "hasValue") {
        this.setData({
          currentCursor: 0
        })
      }
      wx.request({

        url: 'https://xcx.benliang.wang/xcx/GetAllClassServlet',
        method: 'POST',
        data: {
          cursor: that.data.currentCursor

        },
        success: function (res) {
          if (res.data.status == 100) {
            wx.showToast({
              title: '无更多数据',
              icon: 'none',
              duration: 2000
            })
          } else if (res.data.status == 200) {
            that.setData({
              courseList: res.data.class
            })
          }
          console.log(res.data)
          wx.hideLoading()
        },
        fail: function (res) {
          wx.hideLoading()
        }
      })
    }
    else {
      if (this.data.preMode == "hasNot") {
        this.setData({
          currentCursor: 0
        })
      }
      wx.request({

        url: 'https://xcx.benliang.wang/xcx/SearchClassServlet',

        method: 'POST',
        data: {
          keyword: that.data.searchValue,
          cursor: that.data.currentCursor
        },
        success: function (res) {
          if (res.data.status == 200) {
            that.setData({
              courseList: res.data.class
            })
          } else {
            wx.showToast({
              title: '无更多数据',
              icon: 'none',
              duration: 2000
            })
          }
          wx.hideLoading()

        },
        fail: function (res) {
          wx.hideLoading()
        }
      })
    }
    setTimeout(function () {
      wx.stopPullDownRefresh()
    }, 1000)
    wx.pageScrollTo({
      scrollTop: 0,
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

    console.log("bbbb")
    wx.showLoading({
      title: '数据加载中',
    })
    this.setData({
      currentCursor: this.data.currentCursor + 10
    })
    var that = this
    console.log("bottomRefresh:  " + this.data.searchValue)
    if (this.data.searchValue == '') {
      wx.request({

        url: 'https://xcx.benliang.wang/xcx/GetAllClassServlet',
        method: 'POST',
        data: {
          cursor: that.data.currentCursor

        },
        success: function (res) {
          if (res.data.status == 100) {
            wx.showToast({
              title: '无更多数据',
              duration: 2000,
              icon: 'none'
            })

            that.setData({
              currentCursor: that.data.currentCursor - 10
            })
          } else if (res.data.status == 200) {
            
            that.setData({
              courseList: that.data.courseList.concat(res.data.class)
            })
          }
          console.log(res.data)
          wx.hideLoading()
        },
        fail: function (res) {
          wx.hideLoading()
        }
      })
    }
    else {
      wx.request({

        url: 'https://xcx.benliang.wang/xcx/SearchClassServlet',

        method: 'POST',
        data: {
          keyword: that.data.searchValue,
          cursor: that.data.currentCursor
        },
        success: function (res) {
          if (res.data.status == 200) {
            that.setData({
              courseList: that.data.courseList.concat(res.data.class)
            })
          } else {
            wx.showToast({
              title: '无更多数据',
              icon: 'none',
              duration: 2000

            })

            that.setData({
              currentCursor: that.data.currentCursor - 10

            })
          }
          wx.hideLoading()

        },
        fail: function (res) {
          wx.hideLoading()
        }
      })
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  searchInput: function (e) {
    console.log("search detail" + e.detail.value);
    this.setData({
      searchValue: e.detail.value
    })
  },

  searchTap: function () {
    this.setData({
      currentCursor: 0
    })
    var that = this
    console.log("searchTap:  " + this.data.searchValue)
    if (this.data.searchValue == '') {
      wx.request({

        url: 'https://xcx.benliang.wang/xcx/GetAllClassServlet',

        method: 'POST',
        data: {
          cursor: this.data.currentCursor
        },
        success: function (res) {
          if (res.data.status == 100) {
            wx.showToast({
              title: '无相应课程',
              icon: 'none',
              duration: 2000
            })
          } else if (res.data.status == 200) {
            wx.hideLoading()
            that.setData({
              courseList: res.data.class
            })
          }
          console.log(res.data)
        },
        fail: function (res) {
          wx.hideLoading()
        }
      })
    }
    else {
      wx.request({

        url: 'https://xcx.benliang.wang/xcx/SearchClassServlet',

        method: 'POST',
        data: {
          keyword: that.data.searchValue,
          cursor: that.data.currentCursor
        },
        success: function (res) {
          if (res.data.status == 200) {
            that.setData({
              courseList: res.data.class
            })
          } else if (res.data.status == 150){
            that.setData({
              courseList: []
            })
            wx.showToast({
              title: '课程不存在',
              icon:'none'
            })
          }

        },
        fail: function (res) {

        }
      })
    }

  },
  courseClick: function (e) {
    console.log(e.currentTarget.id)
    var _id = e.currentTarget.id
    var _class = e.currentTarget.dataset.class
    wx.navigateTo({
      url: '/pages/course/course?id=' + _id + "&showmodal=" + false,
    })
  },


})