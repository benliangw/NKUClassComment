Component({
  properties: {
    // 这里定义了自定义组件属性，可以用于渲染
    key: {
      type: Number,
      value: 0,
    }
  },
  data: {
    // 这里是一些组件内部数据
    stars: [0, 1, 2, 3, 4],
    normalSrc: '/images/normal.png',
    selectedSrc: '/images/selected.png',
    halfSrc: '/images/half.png'
  },
  methods: {
    // selectLeft: function (e) {
    //   var key = e.currentTarget.dataset.key
    //   // if (this.data.key == 0.5 && e.currentTarget.dataset.key == 0.5) {
    //   //   //只有一颗星的时候,再次点击,变为0颗
    //   //   key = 0;
    //   // }
    //   console.log("得" + key + "分")
    //   this.setData({
    //     key: key
    //   })

    // },
    // //点击右边,整颗星
    // selectRight: function (e) {
    //   var key = e.currentTarget.dataset.key
    //   console.log("得" + key + "分")
    //   this.setData({
    //     key: key
    //   })
    // }

  }
})