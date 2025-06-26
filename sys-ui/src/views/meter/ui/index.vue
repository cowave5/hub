<template>
  <div class="app-container" style="height: 100vh; display: flex; flex-direction: column;">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input placeholder="搜索操作"
                    clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px"/>
        </div>
        <div class="head-container">
          <button @click="toggleCommon" class="toggle-btn">
            {{ openCommon ? '▼  通用操作' : '▶  通用操作' }}
          </button>
          <transition name="collapse">
            <div v-show="openCommon">
              <div class="circle-row">
                <div class="dnd-item" @mousedown="startDrag('start-node', '开始', $event)">
                  <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 50 50">
                    <circle cx="25" cy="25" r="24" fill="#f1fbfc4d" stroke="#A2B1C3" stroke-width="2"/>
                    <text x="25" y="25" text-anchor="middle" alignment-baseline="middle" font-size="14" fill="#000">
                      开始
                    </text>
                  </svg>
                </div>
                <div class="dnd-item" @mousedown="startDrag('pause-node', '等待', $event)">
                  <svg width="50" height="50" viewBox="0 0 50 50" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="25" cy="25" r="24" stroke="#A2B1C3" stroke-width="2" fill="#f1fbfc4d"/>
                    <text x="50%" y="50%" text-anchor="middle" dominant-baseline="middle" font-size="14" fill="#000">
                      等待
                    </text>
                    <rect x="21" y="33" width="3" height="12" rx="1" fill="#383737"/>
                    <rect x="27" y="33" width="3" height="12" rx="1" fill="#383737"/>
                  </svg>
                </div>
                <div class="dnd-item" @mousedown="startDrag('end-node', '结束', $event)">
                  <svg width="50" height="50" viewBox="0 0 50 50" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="25" cy="25" r="24" stroke="#A2B1C3" stroke-width="2" fill="#f1fbfc4d"/>
                    <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" font-size="14" fill="#000">
                      结束
                    </text>
                    <rect x="19" y="33" width="12" height="12" rx="1" fill="#383737"/>
                  </svg>
                </div>
              </div>
              <div class="other-row">
                <div class="dnd-item" @mousedown="startDrag('gateway-node', '网关', $event)">
                  <svg width="60" height="50" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 60 50">
                    <polygon points="30,0 60,25 30,50 0,25" stroke="#A2B1C3" stroke-width="2" fill="#f1fbfc4d"/>
                    <text x="30" y="25" text-anchor="middle" dominant-baseline="middle" fill="#000" font-size="14">
                      网关
                    </text>
                  </svg>
                </div>
                <div class="dnd-item" @mousedown="startDrag('judge-node', '判断', $event)">
                  <svg width="120" height="40" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 120 40">
                    <polygon points="60,0 120,20 60,40 0,20" stroke="#A2B1C3" stroke-width="2" fill="#f1fbfc4d"/>
                    <text x="60" y="20" text-anchor="middle" dominant-baseline="middle" fill="#000" font-size="14">
                      判断
                    </text>
                  </svg>
                </div>
              </div>
              <div class="other-row">
                <div class="dnd-item" @mousedown="startDrag('handle-node', '逻辑处理', $event)">
                  <svg width="100" height="40" viewBox="0 0 100 40" xmlns="http://www.w3.org/2000/svg">
                    <rect x="1" y="1" width="98" height="38" rx="6" ry="6" fill="#f1fbfc4d" stroke="#A2B1C3"
                          stroke-width="2"/>
                    <text x="50" y="20" font-size="14" fill="#000" text-anchor="middle" alignment-baseline="middle"
                          font-family="Arial, sans-serif">逻辑处理
                    </text>
                  </svg>
                </div>
                <div class="dnd-item" @mousedown="startDrag('manual-node', '人工操作', $event)">
                  <svg width="100" height="60" viewBox="0 0 100 40" xmlns="http://www.w3.org/2000/svg">
                    <polygon points="20,0 100,0 80,40 0,40" fill="#f1fbfc4d" stroke="#A2B1C3" stroke-width="2"/>
                    <text x="50" y="20" font-size="14" fill="#000" text-anchor="middle" alignment-baseline="middle"
                          font-family="Arial, sans-serif">人工操作
                    </text>
                  </svg>
                </div>
              </div>
            </div>
          </transition>
        </div>
      </el-col>
      <el-col :span="20" :xs="24">
        <el-button type="primary" plain  size="mini" icon="el-icon-plus" @click="toSvg">导出</el-button>
        <el-button type="success" plain  size="mini" icon="el-icon-plus" @click="graphSave">保存</el-button>
        <div ref="container" id="container" style="margin-top: 20px; height: calc(100vh - 40px); overflow: auto; position: relative;"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {Graph} from '@antv/x6'
import {Export} from '@antv/x6-plugin-export'
import {Transform} from '@antv/x6-plugin-transform'
import {Snapline} from '@antv/x6-plugin-snapline'
import {Clipboard} from '@antv/x6-plugin-clipboard'
import {Keyboard} from '@antv/x6-plugin-keyboard'
import {Selection} from '@antv/x6-plugin-selection'
import {History} from '@antv/x6-plugin-history'
import {Dnd} from '@antv/x6-plugin-dnd'

export default {
  name: "Config",
  dicts: ['yes_no'],
  data() {
    return {
      openCommon: true,
      graph: null,
      dnd: null,
      saveData: [],
      saveId: '',
      saveVal: '',
      canRedo: false, // 恢复
      canUndo: false, // 撤销
      data: {

      }
    };
  },
  created() {

  },
  mounted() {
    this.initGraph()
  },
  computed: {
    rules() {
      return {
        configName: [
          {required: true, message: this.$t('config.rules.name'), trigger: "blur"}
        ],
      };
    }
  },
  methods: {
    /** 初始化画布 */
    initGraph() {
      this.graph = new Graph({
        container: this.$refs.container,
        width: this.$refs.container.clientWidth,
        height: this.$refs.container.clientHeight,
        color: '#A2B1C3',
        // 背景
        background: {
          color: '#fff',
        },
        // 禁止移到画布之外
        translating: {
          restrict: true,
        },
        // 画布平移
        panning: {
          enabled: true,
          modifiers: 'shift',
        },
        // 线条样式
        grid: {
          visible: true, // 渲染网格背景
          type: 'doubleMesh',
          args: [
            {
              color: '#eee', // 主网格线颜色
              thickness: 1, // 主网格线宽度
            },
            {
              color: '#ddd', // 次网格线颜色
              thickness: 1, // 次网格线宽度
              factor: 4, // 主次网格线间隔
            },
          ]
        },
        // 缩放
        mousewheel: {
          enabled: true,
          modifiers: 'Ctrl',
          maxScale: 2,
          minScale: 0.5,
        },
        // 连接桩
        highlighting: {
          magnetAdsorbed: {
            name: 'stroke',
            args: {
              attrs: {
                fill: '#5F95FF',
                fillOpacity: '0.3',
                stroke: '#5F95FF',
                strokeOpacity: '0'
              },
            },
          },
        },
        // 连接线
        connecting: {
          snap: true,        // 自动吸附，可以通过配置radius属性来自定义吸附半径
          allowBlank: false, // 是否允许连接到画布空白位置的点
          allowLoop: false,  // 是否允许创建循环连线，即边的起始节点和终止节点为同一节点
          allowNode: true,   // 是否允许边连接到节点（非节点上的连接桩）
          allowEdge: false,  // 是否允许边连接到另一个边
          allowPort: true,   // 是否允许边连接到连接桩
          allowMulti: true,  // 是否允许在相同的起始节点和终止之间创建多条边
          highlight: true,
          router: 'manhattan',
          connector: {
            name: 'rounded',
            args: {
              radius: 6,
            },
          },
          anchor: 'center',
          connectionPoint: 'anchor',
          createEdge() {
            return this.createEdge({
              shape: 'edge',
              router: {
                name: 'manhattan',
                args: {
                  startDirections: ['top', 'right', 'bottom', 'left'],
                  endDirections: ['top', 'right', 'bottom', 'left'],
                },
              },
              connector: {
                name: 'rounded',
              },
              attrs: {
                line: {
                  stroke: '#A2B1C3',
                  strokeWidth: 2,
                  targetMarker: {
                    name: 'block',
                    width: 12,
                    height: 8,
                  },
                },
              },
              zIndex: 1,
              tools: ['edge-editor'], // 文本编辑器
            })
          },
          validateConnection({ targetMagnet }) {
            return !!targetMagnet
          },
        },
      });
      // 处理节点
      Graph.registerNode('handle-node', {
        inherit: 'rect',
        width: 100,
        height: 40,
        label: '逻辑处理',
        markup: [
          {tagName: 'rect', selector: 'body'},
          {tagName: 'text', selector: 'label'},
        ],
        attrs: {
          body: {
            stroke: '#A2B1C3',
            fill: '#f1fbfc4d',
            strokeWidth: 2,
            rx: 6,
            ry: 6
          },
        },
        ports: {
          groups: {
            top: {
              position: 'top',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
            bottom: {
              position: 'bottom',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
            left: {
              position: 'left',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
            right: {
              position: 'right',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
          },
          items: [
            {id: 'port', group: 'bottom'},
            {id: 'port2', group: 'top'},
            {id: 'port3', group: 'left'},
            {id: 'port4', group: 'right'}
          ],
        },
      }, true);
      // 人工操作
      Graph.registerNode('manual-node', {
        inherit: 'polygon',
        label: '人工操作',
        width: 100,
        height: 40,
        markup: [
          {tagName: 'polygon', selector: 'body'},
          {tagName: 'text', selector: 'label'},
        ],
        attrs: {
          body: {
            refPoints: '20,0 100,0 80,60 0,60',
            stroke: '#A2B1C3',
            fill: '#f1fbfc4d',
            strokeWidth: 2,
          },
          label: {
            textAnchor: 'middle',
            textVerticalAnchor: 'middle',
            fill: '#000',
            fontSize: 14,
            refX: '50%',
            refY: '50%',
          },
        },
        ports: {
          groups: {
            top: {
              position: 'top',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            right: {
              position: {
                name: 'absolute',
                args: {x: 90, y: 20},
              },
              attrs: {
                circle: {
                  magnet: true,
                  r: 3,
                  stroke: 'black',
                  fill: '#fff',
                },
              },
            },
            bottom: {
              position: 'bottom',
              attrs: {
                circle: {
                  magnet: true,
                  r: 3,
                  stroke: 'black',
                  fill: '#fff',
                },
              },
            },
            left: {
              position: {
                name: 'absolute',
                args: {x: 10, y: 20},
              },
              attrs: {
                circle: {
                  magnet: true,
                  r: 3,
                  stroke: 'black',
                  fill: '#fff',
                },
              },
            },
          },
          items: [
            {id: 'port1', group: 'top'},
            {id: 'port2', group: 'right'},
            {id: 'port3', group: 'bottom'},
            {id: 'port4', group: 'left'},
          ],
        },
      }, true);
      // 开始节点
      Graph.registerNode('start-node', {
        inherit: 'circle',
        label: '开始',
        width: 50,
        height: 50,
        markup: [
          {tagName: 'circle', selector: 'body'},
          {tagName: 'text', selector: 'label'},
        ],
        attrs: {
          body: {
            stroke: '#A2B1C3',
            fill: '#f1fbfc4d',
            strokeWidth: 2,
          },
        },
        ports: {
          groups: {
            top: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: -90,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            right: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 0,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            bottom: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 90,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            left: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 180,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
          },
          items: [
            {id: 'port1', group: 'top'},
            {id: 'port2', group: 'right'},
            {id: 'port3', group: 'bottom'},
            {id: 'port4', group: 'left'},
          ],
        },
      }, true);
      // 结束节点
      Graph.registerNode('end-node', {
        inherit: 'circle',
        label: '结束',
        width: 50,
        height: 50,
        markup: [
          {tagName: 'circle', selector: 'body'},
          {tagName: 'text', selector: 'label'},
          {tagName: 'rect', selector: 'bar1'},
        ],
        attrs: {
          body: {
            stroke: '#A2B1C3',
            fill: '#f1fbfc4d',
            strokeWidth: 2,
          },
          bar1: {
            fill: '#383737',
            x: 19,
            y: 33,
            width: 12,
            height: 12,
            rx: 1,
          },
        },
        ports: {
          groups: {
            top: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: -90,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            right: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 0,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            bottom: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 90,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            left: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 180,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
          },
          items: [
            {id: 'port1', group: 'top'},
            {id: 'port2', group: 'right'},
            {id: 'port3', group: 'bottom'},
            {id: 'port4', group: 'left'},
          ],
        },
      }, true);
      // 暂停节点
      Graph.registerNode('pause-node', {
        inherit: 'circle',
        label: '等待',
        width: 50,
        height: 50,
        markup: [
          {tagName: 'circle', selector: 'body'},
          {tagName: 'text', selector: 'label'},
          {tagName: 'rect', selector: 'bar1'},
          {tagName: 'rect', selector: 'bar2'},
        ],
        attrs: {
          body: {
            stroke: '#A2B1C3',
            strokeWidth: 2,
            fill: '#f1fbfc4d',
          },
          bar1: {
            fill: '#383737',
            x: 21,
            y: 33,
            width: 3,
            height: 12,
            rx: 1,
          },
          bar2: {
            fill: '#383737',
            x: 27,
            y: 33,
            width: 3,
            height: 12,
            rx: 1,
          },
        },
        ports: {
          groups: {
            top: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: -90,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            right: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 0,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            bottom: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 90,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            left: {
              position: {
                name: 'ellipseSpread',
                args: {
                  start: 180,
                  step: 90,
                },
              },
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
          },
          items: [
            {id: 'port1', group: 'top'},
            {id: 'port2', group: 'right'},
            {id: 'port3', group: 'bottom'},
            {id: 'port4', group: 'left'},
          ],
        },
      }, true);
      // 判断节点
      Graph.registerNode('judge-node', {
        inherit: 'polygon',
        label: '判断',
        width: 120,
        height: 40,
        markup: [
          {tagName: 'polygon', selector: 'body'},
          {tagName: 'text', selector: 'label'},
        ],
        attrs: {
          body: {
            refPoints: '0,40 40,0 80,40 40,80', // 菱形的四个点
            stroke: '#A2B1C3',
            fill: '#f1fbfc4d',
            strokeWidth: 2,
          },
          label: {
            textAnchor: 'middle',
            textVerticalAnchor: 'middle',
            fill: '#000',
            fontSize: 14,
            refX: '50%',
            refY: '50%',
          },
        },
        ports: {
          groups: {
            top: {
              position: {name: 'ellipseSpread', args: {start: -90, step: 90}},
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            right: {
              position: {name: 'ellipseSpread', args: {start: 0, step: 90}},
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            bottom: {
              position: {name: 'ellipseSpread', args: {start: 90, step: 90}},
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
            left: {
              position: {name: 'ellipseSpread', args: {start: 180, step: 90}},
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  fill: '#fff',
                  r: 3,
                },
              },
            },
          },
          items: [
            {id: 'port1', group: 'top'},
            {id: 'port2', group: 'right'},
            {id: 'port3', group: 'bottom'},
            {id: 'port4', group: 'left'},
          ],
        },
      }, true);
      // 网关节点
      Graph.registerNode('gateway-node', {
        inherit: 'polygon',
        label: '网关',
        width: 60,
        height: 50,
        attrs: {
          body: {
            refPoints: '30,0 60,30 30,60 0,30', // 标准菱形
            stroke: '#A2B1C3',
            strokeWidth: 2,
            fill: '#f1fbfc4d',
          },
          label: {
            textAnchor: 'middle',
            textVerticalAnchor: 'middle',
            fill: '#000',
            fontSize: 14,
            refX: '50%',
            refY: '50%',
          },
        },
        ports: {
          groups: {
            top: {
              position: 'top',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
            bottom: {
              position: 'bottom',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
            left: {
              position: 'left',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
            right: {
              position: 'right',
              attrs: {
                circle: {
                  magnet: true,
                  stroke: 'black',
                  r: 3,
                },
              },
            },
          },
          items: [
            {id: 'port1', group: 'bottom'},
            {id: 'port2', group: 'top'},
            {id: 'port3', group: 'left'},
            {id: 'port4', group: 'right'}
          ],
        },
      });
      // 允许调整
      this.graph.use(
          new Transform({
            // 调整大小
            resizing: {
              enabled: true
            },
            // 旋转角度
            rotating: {
              enabled: false
            }
          }),
      );
      // 允许拖拽
      this.dnd = new Dnd({
        target: this.graph,
        scaled: false,
      });
      // 允许导出
      this.graph.use(new Export());
      // 框选移动
      this.graph.use(
          new Selection({
            enabled: true,
            multiple: true,
            movable: true,
            rubberband: true,
            showNodeSelectionBox: true,
            showEdgeSelectionBox: true,
            pointerEvents: 'all',
          }),
      );
      // 监听历史
      this.graph.on('history:change', () => {
        this.canRedo = this.graph.canRedo();
        this.canUndo = this.graph.canUndo()
      });
      // 鼠标移入
      this.graph.on('node:mouseenter', () => {
        const container = document.getElementById('container')
        const ports = container.querySelectorAll('.x6-port-body');
        this.showPorts(ports, true)
      });
      // 鼠标移出
      this.graph.on('node:mouseleave', () => {
        const container = document.getElementById('container')
        const ports = container.querySelectorAll('.x6-port-body')
        this.showPorts(ports, false)
      });
      // 鼠标点击空白处
      this.graph.on('blank:mousedown', () => {
        const container = document.getElementById('container')
        const ports = container.querySelectorAll('.x6-port-body')
        this.showPorts(ports, false)
      });
      // 对齐线
      this.graph.use(new Snapline({
            enabled: true,
            clean: false, // true会3s后清除对齐线，false不会清除，数字则在指定时间后清除对齐线
          }),
      );
      // 复制粘贴
      this.graph.use(new Clipboard({
            enabled: true,
            global: false //是否为全局键盘事件
          }),
      );
      //撤销
      this.graph.use(new History({
            enabled: true,
          }),
      );
      // 快捷键
      this.graph.use(new Keyboard({
            enabled: true
          }),
      );
      // 复制
      this.graph.bindKey('ctrl+c', () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.copy(cells)
        }
        return false
      });
      // 剪切
      this.graph.bindKey('ctrl+x', () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.copy(cells)
          this.graph.removeCells(cells)
        }
      });
      // 粘贴
      this.graph.bindKey('ctrl+v', () => {
        if (!this.graph.isClipboardEmpty()) {
          const cells = this.graph.paste({offset: 32})
          this.graph.cleanSelection()
          this.graph.select(cells)
        }
        return false
      });
      // 撤销
      this.graph.bindKey('ctrl+z', () => {
        this.graph.undo()
      });
      // 恢复
      this.graph.bindKey('ctrl+y', () => {
        this.graph.redo()
      });
      // 删除
      this.graph.bindKey('Backspace', () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.removeCells(cells)
        }
        return false
      });
      // 节点右键
      this.graph.on('node:contextmenu', ({e, x, y, cell, view}) => {
        this.onContextmenu(e, x, y, cell, view)
      });
      // 连线右击
      this.graph.on('edge:contextmenu', ({e, x, y, cell, view}) => {
        this.onContextmenu(e, x, y, cell, view)
      });
      // 空白右击
      this.graph.on('blank:contextmenu', ({e, x, y, cell, view}) => {
        this.onContextmenu(e, x, y, cell, view)
      });
      // 绑定数据
      this.graph.fromJSON(this.data);
    },
    /** 桩点显示/隐藏 */
    showPorts(ports, show) {
      for (let i = 0, len = ports.length; i < len; i = i + 1) {
        ports[i].style.visibility = show ? 'visible' : 'hidden'
      }
    },
    /** 右键菜单 */
    onContextmenu(event, x, y, cell, view) {
      const isRedo = this.canRedo;
      const isUndo = this.canUndo;
      const cells = this.graph.getSelectedCells();
      const items = [
          {
            label: "复制 ctrl+z",
            disabled: cells.length === 0,
            onClick: () => {
              if (cells.length) {
                this.graph.copy(cells)
              }
            }
          },
          {
            label: "粘贴 ctrl+c ",
            disabled: this.graph.isClipboardEmpty(),
            onClick: () => {
              if (!this.graph.isClipboardEmpty()) {
                const cells = this.graph.paste({offset: 32})
                this.graph.cleanSelection()
                this.graph.select(cells)
              }
            }
          },
          {
            label: "剪切 ctrl+x",
            divided: true,
            disabled: cells.length === 0,
            onClick: () => {
              if (cells.length) {
                this.graph.copy(cells)
                this.graph.removeCells(cells)
              }
            }
          },
          {
            label: "撤销 ctrl+z",
            disabled: !isUndo,
            onClick: () => {
              this.undo()
            }
          },
          {
            label: "恢复 ctrl+y",
            disabled: !isRedo,
            divided: true,
            onClick: () => {
              this.redo()
            }
          },
          {
            label: "删除 backspace",
            disabled: cells.length === 0,
            onClick: () => {
              if (cells.length) {
                this.graph.removeCells(cells)
              }
            }
          }
      ];
      // 节点编辑
      if (this.graph.isNode(cell)) {
        items.unshift({
          label: "编辑",
          disabled: false,
          onClick: () => {
            alert(1);
          }
        });
      }
      this.$contextmenu({
        items: items,
        event, // 鼠标事件信息
        customClass: "custom-class", // 自定义菜单 class
        zIndex: 100, // 菜单样式 z-index
        minWidth: 150 // 主菜单最小宽度
      });
      return false;
    },
    /** 撤销 */
    undo() {
      this.graph.undo()
    },
    /** 恢复 */
    redo() {
      this.graph.redo()
    },
    /** 创建节点，移入画布 */
    startDrag(shape, label, e) {
      const node = this.graph.createNode({
        shape: shape,
        label: label,
        data: {
          minNum: 0,
          maxNum: 0
        }
      })
      this.dnd.start(node, e)
    },
    /** 保存画布 */
    graphSave() {
      this.saveData = this.graph.toJSON()
      const saveDataTrans = JSON.stringify(this.saveData);
      // 存到库中
      console.log(saveDataTrans);

    },
    /** 通用模板展开/收起 */
    toggleCommon() {
      this.openCommon = !this.openCommon;
    },




    // 导出svg
    toSvg() {
      this.graph.exportSVG('导出svg格式')
    },
    // 导出png
    toJpg() {
      this.graph.exportPNG('导出png格式')
    },
    // 销毁画布
    graphDispose() {
      this.graph.dispose()
    },
    // 将画布中元素居中展示
    graphCenter() {
      this.graph.centerContent()
    },
    // 添加节点
    graphAddNode() {
      this.graph.addNode({
        shape: 'custom-node',
        x: 10,
        y: 10,
        width: 100,
        height: 40,
        label: 'hello',
      })
    },
    // 修改节点
    graphUpdate() {
      const nodes = this.graph.getNodes()
      nodes.forEach((node) => {
        const width = 100 + Math.floor(Math.random() * 50)
        const height = 40 + Math.floor(Math.random() * 10)
        node.prop('size', {width, height})
        const color = this.color16()
        node.attr('body/fill', color)
      })
    },
    // 十六进制颜色随机
    color16() {
      var r = Math.floor(Math.random() * 256);
      var g = Math.floor(Math.random() * 256);
      var b = Math.floor(Math.random() * 256);
      var color = "#" + r.toString(16) + g.toString(16) + b.toString(16);
      return color;
    },
    // 重新渲染
    graphRecreate(val) {
      if (val === undefined || val === null || val === '') {
        this.initGraph()
      } else {
        this.saveData = JSON.parse(val.chartCode)
        this.saveId = val.id
        this.saveVal = val
        this.graph.fromJSON(this.saveData)
        this.$message({
          message: '渲染成功',
          type: 'success'
        })
      }
    },
    // 弹框赋值
    updateByAlert(min, max, cell) {
      if (min === "") {
        min = 0
      }
      if (max === "") {
        max = 0
      }
      cell.setData({minNum: min, maxNum: max}, {overwrite: true})
      cell.label = cell.label.substr(0, cell.label.lastIndexOf("\n")) + "\n(" + min + "%~" + max + "%)"
    },
  }
};
</script>

<style>
  .head-container > div {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
  }
  /* 三个圆形节点容器 */
  .circle-row {
    display: flex;
    gap: 16px;
    width: 100%;
    justify-content: start; /* 可调整为center或space-around */
  }
  /* 其他两个节点容器 */
  .other-row {
    display: flex;
    gap: 16px;
    width: 100%;
    justify-content: start;
  }
  .dnd-item {
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    user-select: none;
  }
  .dnd-item svg {
    max-width: 100%;
  }

  .toggle-btn {
    background: none;
    color: #635e5e;
    cursor: pointer;
    width: 100%;
    border-left: none;
    border-right: none;
    border-top: 1px solid #d9e1ec;
    border-bottom: 1px solid #d9e1ec;
    font-size: 16px;
    font-weight: 500;
    text-align: left;
    padding: 4px 8px;
    margin-bottom: 8px;
  }
  .toggle-btn:hover {
    background-color: #d9e1ec; /* 浅灰色背景，可自定义颜色 */
  }
  /* 过渡动画 */
  .collapse-enter-active, .collapse-leave-active {
    transition: max-height 0.3s ease;
  }
  .collapse-enter, .collapse-leave-to /* .collapse-leave-active in <2.1.8 */
  {
    max-height: 0;
    overflow: hidden;
  }
  .collapse-enter-to, .collapse-leave {
    max-height: 500px; /* 设个大一点，保证内容都能显示 */
    overflow: hidden;
  }
</style>
