<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>Open Hub</h2>
        <p>
          <b>当前版本:</b> <span>{{ version }}</span>
        </p>
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>
<script>
export default {
  name: "Index",
  dicts: [],
  data() {
    return {
      version: "",
    };
  },
  created() {
    this.version = process.env.VUE_APP_VERSION;
  },
  methods: {

  },
};
</script>

<style scoped lang="scss">
.el-table {
  cursor: pointer;
}

.red-point{
  position: relative;
}

.red-point::before{
  content: " ";
  border: 4px solid #1890ff;
  border-radius:3px;
  position: absolute;
  z-index: 1000;
  left: 0;
  top: 1px;
  margin-left: -10px;
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
    padding-bottom: 2px;
  }

  h3 {
    margin-top: 0px;
    font-size: 15px;
    font-weight: 300;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .permission-toggle-enter-active, .permission-toggle-leave-active {
    transition: opacity 1s, transform 1s;
  }

  .permission-toggle-enter, .permission-toggle-leave-to /* .permission-toggle-leave-active below version 2.1.8 */ {
    opacity: 0;
    transform: translateY(-10px);
  }
}
</style>

