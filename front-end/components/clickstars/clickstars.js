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
    selectStar :function (e) {
      var _key = e.currentTarget.dataset.key;
      this.setData({
        key: _key
      })
    }

  }
})