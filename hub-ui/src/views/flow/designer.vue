<template>
  <div class="e_centent">
    <div class="typelise">
    </div>
    <div v-show="type === 0" class="mainse">
      <k-form-design :showHead="false" style="height: 100%;" />
    </div>
    <div v-show="type === 1" class="mainse">
      <iframe :src="src" frameborder="0" width="100%" height="100%"/>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      type: 1,
      src: '',
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id;
    this.getSrc(id)
  },
  methods: {
    getSrc(id) {
      if (process.env.NODE_ENV === 'production') {
        this.src = 'http://' + location.hostname +':82/admin/designer/index.html#/editor/' + id;
      }else{
        this.src = 'http://localhost:19010/admin/designer/index.html#/editor/' + id;
      }
    },
  }
}
</script>

<style scoped="scoped" lang="scss">
.e_centent {
  height: 900px;
  background: #FFF;

  .typelise {
    border-bottom: 2px solid #ddd;
    .item {
      line-height: 40px;
      display: inline-block;
      width: 100px;
      text-align: center;
      cursor: pointer;

      &.x {
        color: #0000FF;
      }
    }
  }
}

.mainse {
  height: calc(100% - 42px);
}
</style>
