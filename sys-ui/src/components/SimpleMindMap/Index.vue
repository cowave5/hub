<template>
  <div class="container">
    <template v-if="show">
      <Toolbar v-if="!isZenMode"></Toolbar>
      <Edit v-model="mindMapData"></Edit>
    </template>
  </div>
</template>

<script>
import Toolbar from './components/Toolbar'
import Edit from './components/Edit'
import { mapState, mapActions, mapMutations } from 'vuex'
import { getData,getLocalConfig } from '@/components/SimpleMindMap/api'

export default {
  name: 'Index',
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
      default: "",
    },
    /* 只读 */
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  components: {
    Toolbar,
    Edit
  },
  data() {
    return {
      show: false,
      mindMapData: JSON.parse(this.value),
      mindMapDataInit: {
          "mindMapData": {
            "root": {
                "data": {
                    "text": "根节点",
                    "expand": true,
                    "isActive": false
                },
                "children": [
                    {
                        "data": {
                            "text": "二级节点",
                            "generalization": {
                                "text": "概要",
                                "expand": true,
                                "isActive": false
                            },
                            "expand": true,
                            "isActive": false
                        },
                        "children": [
                            {
                                "data": {
                                    "text": "分支主题",
                                    "expand": true,
                                    "isActive": false
                                },
                                "children": []
                            },
                            {
                                "data": {
                                    "text": "分支主题",
                                    "expand": true,
                                    "isActive": false
                                },
                                "children": []
                            }
                        ]
                    },
                    {
                        "data": {
                            "text": "二级节点",
                            "expand": true,
                            "isActive": false
                        },
                        "children": []
                    }
                ]
            },
            "theme": {
                "template": "classic4",
                "config": {}
            },
            "layout": "logicalStructure",
            "config": {},
            "view": {
                "transform": {
                    "scaleX": 1,
                    "scaleY": 1,
                    "shear": 0,
                    "rotate": 0,
                    "translateX": -215,
                    "translateY": 9,
                    "originX": 0,
                    "originY": 0,
                    "a": 1,
                    "b": 0,
                    "c": 0,
                    "d": 1,
                    "e": -215,
                    "f": 9
                },
                "state": {
                    "scale": 1,
                    "x": -215,
                    "y": 9,
                    "sx": -212,
                    "sy": 8
                }
            }
        }
      },
    }
  },
  computed: {
    isZenMode(){return this.$store.state.SimpleMindMap.localConfig.isZenMode}
  },
  async created() {
    await this.initLocalConfig()
    const loading = this.$loading({
      lock: true,
      text: '正在加载，请稍后...'
    })
    await this.getUserMindMapData()
    this.show = true
    loading.close()
  },
  beforeDestroy() {
    var data = localStorage.getItem('SIMPLE_MIND_MAP_DATA')
    this.$emit("input",data)
    localStorage.setItem('SIMPLE_MIND_MAP_DATA', getData())
  },
  methods: {
    ...mapActions(['getUserMindMapData']),
    setLocalConfig(payLoad){
      this.$store.commit('setLocalConfig',payLoad)
    },

    /**
     * @Author: 王林25
     * @Date: 2022-11-14 19:07:03
     * @Desc: 初始化本地配置
     */
    initLocalConfig() {
      let config = getLocalConfig()
      if (config) {
        this.setLocalConfig({
          ...this.$store.state.localConfig,
          ...config
        })
      }
    },
  }
}
</script>

<style lang="less" scoped>
.container {
}
</style>
