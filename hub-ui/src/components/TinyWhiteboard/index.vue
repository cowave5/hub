<template>
  <div ref="container" class="container">
    <div class="canvasBox" ref="box"></div>
    <div class="toolbar" v-if="!readonly">
      <el-radio-group v-model="currentType" @change="onCurrentTypeChange">
        <el-radio-button label="selection">选择</el-radio-button>
        <el-radio-button label="rectangle">矩形</el-radio-button>
        <el-radio-button label="diamond">菱形</el-radio-button>
        <el-radio-button label="triangle">三角形</el-radio-button>
        <el-radio-button label="circle">圆形</el-radio-button>
        <el-radio-button label="line">线段</el-radio-button>
        <el-radio-button label="arrow">箭头</el-radio-button>
        <el-radio-button label="freedraw">自由画笔</el-radio-button>
        <el-radio-button label="text">文字</el-radio-button>
        <el-radio-button label="image">图片</el-radio-button>
      </el-radio-group>
    </div>
    <Transition>
      <div class="sidebar" v-show="activeElement || hasSelectedElements">
        <div class="elementStyle">
          <el-row :gutter="20">
            <el-col :span="12">
              <!-- 描边 -->
              <div class="styleBlock" v-if="!['text', 'image'].includes(activeElementType) || hasSelectedElements " >
                <div class="styleBlockTitle">描边</div>
                <div class="styleBlockContent">
                  <el-color-picker v-model="activeElementStyle" @change="updateStyle('strokeStyle', $event)" size="small"></el-color-picker>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <!-- 填充 -->
              <div
                class="styleBlock"
                v-if="
                  !['image', 'line', 'arrow', 'freedraw'].includes(
                    activeElementType
                  ) || hasSelectedElements
                "
              >
                <div class="styleBlockTitle">填充</div>
                <div class="styleBlockContent">
                  <el-color-picker v-model="activeElementFillStyle" @change="updateStyle('fillStyle', $event)" size="small"></el-color-picker>
                </div>
              </div>
            </el-col>
          </el-row>


          <!-- 字体 -->
          <div
            class="styleBlock"
            v-if="['text'].includes(activeElementType) || hasSelectedElements"
          >
            <div class="styleBlockTitle">字体</div>
            <div class="styleBlockContent">
              <el-select
                size="mini"
                v-model="fontFamily"
                placeholder="字体"
                @change="updateStyle('fontFamily', $event)"
              >
                <el-option
                  v-for="item in fontFamilyList"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                  :style="{ fontFamily: item.value }"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <!-- 字号 -->
          <div
            class="styleBlock"
            v-if="['text'].includes(activeElementType) || hasSelectedElements"
          >
            <div class="styleBlockTitle">字号</div>
            <div class="styleBlockContent">
              <el-select
                size="mini"
                v-model="fontSize"
                placeholder="字号"
                @change="updateStyle('fontSize', $event)"
              >
                <el-option
                  v-for="item in fontSizeList"
                  :key="item.value"
                  :label="item.name"
                  :value="item.value"
                  :style="{ fontSize: item.value }"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <!-- 描边宽度 -->
          <div
            class="styleBlock"
            v-if="
              !['image', 'text'].includes(activeElementType) ||
              hasSelectedElements
            "
          >
            <div class="styleBlockTitle">描边宽度</div>
            <div class="styleBlockContent">
              <el-radio-group
                v-model="lineWidth"
                @change="updateStyle('lineWidth', $event)"
                size="mini"
              >
                <el-radio-button label="small">
                  <div class="lineWidthItem small">
                    <div class="bar"></div>
                  </div>
                </el-radio-button>
                <el-radio-button label="middle">
                  <div class="lineWidthItem middle">
                    <div class="bar"></div>
                  </div>
                </el-radio-button>
                <el-radio-button label="large">
                  <div class="lineWidthItem large">
                    <div class="bar"></div>
                  </div>
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <!-- 边框样式 -->
          <div
            class="styleBlock"
            v-if="
              !['freedraw', 'image', 'text'].includes(activeElementType) ||
              hasSelectedElements
            "
          >
            <div class="styleBlockTitle">边框样式</div>
            <div class="styleBlockContent">
              <el-radio-group
                v-model="lineDash"
                @change="updateStyle('lineDash', $event)"
                size="mini"
              >
                <el-radio-button :label="0">
                  <div>实线</div>
                </el-radio-button>
                <el-radio-button :label="5">
                  <div>大虚线</div>
                </el-radio-button>
                <el-radio-button :label="2">
                  <div>小虚线</div>
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <!-- 透明度 -->
          <div class="styleBlock">
            <div class="styleBlockTitle">透明度</div>
            <div>
              <el-slider
                v-model="globalAlpha"
                :min="0"
                :max="1"
                :step="0.1"
                @change="updateStyle('globalAlpha', $event)"
              />
            </div>
          </div>
          <!-- 角度 -->
          <div class="styleBlock" v-if="!hasSelectedElements">
            <div class="styleBlockTitle">角度</div>
            <el-row :gutter="20">
              <el-col :span="16">
                <el-slider
                  v-model="rotate"
                  :min="0"
                  :max="360"
                  :step="1"
                  @input="onRotateChange"
                />
              </el-col>
              <el-col :span="8">
                <el-input-number
                  size="mini"
                  style="width: 60px;"
                  :controls="false"
                  v-model="rotate"
                  :min="0"
                  :max="360"
                  @focus="onInputNumberFocus"
                  @blur="onInputNumberBlur"
                  @change="onRotateChange"
                />
              </el-col>
            </el-row>
          </div>
          <!-- 操作 -->
          <div class="styleBlock">
            <div class="styleBlockTitle">操作</div>
            <div class="styleBlockContent">
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                @click="deleteElement"
              />
              <el-button
                type="primary"
                icon="el-icon-document-copy"
                circle
                @click="copyElement"
              />
            </div>
          </div>
        </div>
      </div>
    </Transition>
    <div class="TinyWhiteboard-footerLeft" @click.stop style="display: flex;">
      <!-- 缩放 -->
      <div class="blockBox">
        <el-tooltip effect="light" content="缩小" placement="top">
          <el-button icon="el-icon-zoom-out" circle @click="zoomOut" />
        </el-tooltip>
        <el-tooltip effect="light" content="重置缩放" placement="top">
          <span class="zoom" @click="resetZoom">{{ currentZoom }}%</span>
        </el-tooltip>
        <el-tooltip effect="light" content="放大" placement="top">
          <el-button icon="el-icon-zoom-in" circle @click="zoomIn" />
        </el-tooltip>
      </div>
      <!-- 前进回退 -->
      <div class="blockBox" v-if="!readonly">
        <el-tooltip effect="light" content="回退" placement="top">
          <el-button
            icon="el-icon-refresh-left"
            circle
            :disabled="!canUndo"
            @click="undo"
          />
        </el-tooltip>
        <el-tooltip effect="light" content="前进" placement="top">
          <el-button
            icon="el-icon-refresh-right"
            circle
            :disabled="!canRedo"
            @click="redo"
          />
        </el-tooltip>
      </div>
      <!-- 橡皮擦、显示网格、清空 -->
      <div class="blockBox">
        <!-- 橡皮擦 -->
        <el-tooltip
          effect="light"
          :content="currentType === 'eraser' ? '关闭橡皮擦' : '橡皮擦'"
          placement="top"
        >
          <el-button
            v-if="!readonly"
            icon="el-icon-partly-cloudy"
            circle
            :type="currentType === 'eraser' ? 'primary' : null"
            @click="toggleEraser"
          />
        </el-tooltip>
        <!-- 网格 -->
        <el-tooltip
          effect="light"
          :content="showGrid ? '隐藏网格' : '显示网格'"
          placement="top"
        >
          <el-button
            icon="el-icon-s-grid"
            circle
            :type="showGrid ? 'primary' : null"
            @click="toggleGrid"
          />
        </el-tooltip>
        <!-- 只读、编辑模式切换 -->
        <el-tooltip
          effect="light"
          :content="readonly ? '切换到编辑模式' : '切换到只读模式'"
          placement="top"
        >
          <el-button
            v-show="!readOnly"
            :icon="readonly ? 'el-icon-view' : 'el-icon-edit-outline'"
            circle
            @click="toggleMode"
          />
        </el-tooltip>
        <!-- 清空 -->
        <el-tooltip effect="light" content="清空" placement="top">
          <el-button v-if="!readonly" icon="el-icon-delete" circle @click="empty" />
        </el-tooltip>
      </div>
      <!-- 导入导出 -->
      <div class="blockBox">
        <el-tooltip effect="light" content="从json文件导入" placement="top">
          <el-button
            v-if="!readonly"
            icon="el-icon-upload2"
            circle
            style="margin-right: 10px"
            @click="importFromJson"
          />
        </el-tooltip>
        <el-dropdown @command="handleExportCommand">
          <span class="el-dropdown-link">
            <el-button icon="el-icon-download" circle />
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="png">导出为图片</el-dropdown-item>
              <el-dropdown-item command="json">导出为json</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <!-- 背景 -->
      <div class="blockBox" v-show="!readonly">
        <el-tooltip effect="light" content="背景颜色" placement="top">
          <el-color-picker v-model="backgroundColor" @change="setBackgroundColor" size="small"></el-color-picker>
        </el-tooltip>
      </div>
      <!-- 帮助 -->
      <div class="blockBox" v-show="!readOnly">
        <el-tooltip effect="light" content="帮助" placement="top">
          <el-button
            icon="el-icon-question"
            circle
            style="margin-right: 10px"
            @click="helpDialogVisible = !helpDialogVisible"
          />
        </el-tooltip>
      </div>
      <!-- 滚动 -->
      <div class="blockBox">
        <el-tooltip effect="light" content="滚动至中心" placement="top">
          <el-button @click="scrollToCenter"
            >X:{{ scroll.x }} Y:{{ scroll.y }}
          </el-button>
        </el-tooltip>
      </div>
    </div>
    <!-- 导出图片弹窗 -->
    <el-dialog
      :visible.sync="exportImageDialogVisible"
      title="导出为图片"
      append-to-body
      width="800"
    >
      <div class="exportImageContainer">
        <div class="imagePreviewBox">
          <img :src="exportImageUrl" alt="" />
        </div>
        <div class="handleBox">
          <el-checkbox
            v-model="exportOnlySelected"
            label="仅导出被选中"
            size="large"
            @change="reRenderExportImage"
            style="margin-right: 10px"
          />
          <el-checkbox
            v-model="exportRenderBackground"
            label="背景"
            size="large"
            @change="reRenderExportImage"
            style="margin-right: 10px"
          />
          <el-input
            v-model="exportFileName"
            style="width: 150px; margin-right: 10px"
          ></el-input>
          <el-input-number
            v-model="exportImagePaddingX"
            :min="10"
            :max="100"
            :step="5"
            controls-position="right"
            @change="reRenderExportImage"
            style="margin-right: 10px"
          />
          <el-input-number
            v-model="exportImagePaddingY"
            :min="10"
            :max="100"
            :step="5"
            controls-position="right"
            @change="reRenderExportImage"
            style="margin-right: 10px"
          />
          <el-button type="primary" @click="downloadExportImage"
            >下载</el-button
          >
        </div>
      </div>
    </el-dialog>
    <!-- 导出json弹窗 -->
    <el-dialog
      :visible.sync="exportJsonDialogVisible"
      title="导出为json"
      append-to-body
      width="800"
    >
      <div class="exportJsonContainer">
        <div class="jsonPreviewBox" ref="jsonPreviewBox"></div>
        <div class="handleBox">
          <el-input
            v-model="exportFileName"
            style="width: 150px; margin-right: 10px"
          ></el-input>
          <el-button type="primary" @click="downloadExportJson">下载</el-button>
        </div>
      </div>
    </el-dialog>
    <!-- 帮助弹窗 -->
    <el-dialog :visible.sync="helpDialogVisible" title="帮助" append-to-body width="500">
      <div class="helpDialogContent">
        <p>移动画布：按住空格键进行拖动</p>
        <h2>快捷键</h2>
        <el-table :data="shortcutKeyList">
          <el-table-column property="name" label="操作" />
          <el-table-column property="value" label="快捷键" />
        </el-table>
      </div>
    </el-dialog>
    <!-- 右键菜单 -->
    <Contextmenu v-if="Whiteboard" :app="Whiteboard"></Contextmenu>

  </div>
</template>

<script>
  import TinyWhiteboard from "tiny-whiteboard";
  import Contextmenu from './components/Contextmenu.vue'
  import jsonTree from './libs/jsonTree.js'
  export default {
    name: "TinyWhiteboard",
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
      Contextmenu
    },
    data() {
      return {
        Whiteboard: null,// 应用实例
        currentType: 'selection',// 当前操作类型
        activeElement: null,// 当前激活的元素
        selectedElements: [],// 当前多选的元素
        lineWidth: 'small',// 描边宽度
        fontFamily: '微软雅黑, Microsoft YaHei',// 字体
        fontFamilyList: [
          {
            name: '微软雅黑',
            value: '微软雅黑, Microsoft YaHei'
          },
          {
            name: '宋体',
            value: '宋体, SimSun, Songti SC'
          },
          {
            name: '楷体',
            value: '楷体, 楷体_GB2312, SimKai, STKaiti'
          },
          {
            name: '黑体',
            value: '黑体, SimHei, Heiti SC'
          },
          {
            name: '隶书',
            value: '隶书, SimLi'
          },
          {
            name: 'Andale Mono',
            value: 'andale mono'
          },
          {
            name: 'Arial',
            value: 'arial, helvetica, sans-serif'
          },
          {
            name: 'arialBlack',
            value: 'arial black, avant garde'
          },
          {
            name: 'Comic Sans Ms',
            value: 'comic sans ms'
          },
          {
            name: 'Impact',
            value: 'impact, chicago'
          },
          {
            name: 'Times New Roman',
            value: 'times new roman'
          },
          {
            name: 'Sans-Serif',
            value: 'sans-serif'
          },
          {
            name: 'serif',
            value: 'serif'
          }
        ],
        fontSize: 18,// 字号
        fontSizeList: [10, 12, 16, 18, 24, 32, 48].map(item => {
          return {
            name: item,
            value: item
          }
        }),
        lineDash: 0,// 边框样式
        globalAlpha: 0.1,// 透明度
        rotate: 0,// 角度
        currentZoom: 100,// 当前缩放
        // 缩放允许前进后退
        canUndo: false,
        canRedo: false,
        // 图片导出弹窗
        exportImageDialogVisible: false,
        exportImageUrl: '',
        exportOnlySelected: false,
        exportRenderBackground: true,
        exportFileName: '未命名',
        exportImagePaddingX: 10,
        exportImagePaddingY: 10,
        // json导出弹窗
        exportJsonDialogVisible: false,
        exportJsonData: '',
        tree: null,
        // 背景颜色
        backgroundColor: '',
        // 当前滚动距离
        scroll: { x: 0, y: 0 },
        // 切换显示网格
        showGrid: false,
        // 模式切换
        readonly: false,
        // 帮助弹窗
        helpDialogVisible: false,
        shortcutKeyList: [
          {
            name: '全部选中',
            value: 'Control + a'
          },
          {
            name: '删除',
            value: 'Del 或 Backspace'
          },
          {
            name: '复制',
            value: 'Control + c'
          },
          {
            name: '粘贴',
            value: 'Control + v'
          },
          {
            name: '放大',
            value: 'Control + +'
          },
          {
            name: '缩小',
            value: 'Control + -'
          },
          {
            name: '重置缩放',
            value: 'Control + 0'
          },
          {
            name: '缩放以适应所有元素',
            value: 'Shift + 1'
          },
          {
            name: '撤销',
            value: 'Control + z'
          },
          {
            name: '重做',
            value: 'Control + y'
          },
          {
            name: '显示隐藏网格',
            value: "Control + '"
          }
        ],
        hasSelectedElements: false,
        activeElementStyle: null,
        activeElementType: null,
        activeElementFillStyle: null,
        jsonNull: "{\"state\":{\"scale\":1,\"scrollX\":0,\"scrollY\":0,\"scrollStep\":50,\"backgroundColor\":\"\",\"strokeStyle\":\"#000000\",\"fillStyle\":\"transparent\",\"fontFamily\":\"微软雅黑, Microsoft YaHei\",\"fontSize\":18,\"dragStrokeStyle\":\"#666\",\"showGrid\":false,\"readonly\":false,\"gridConfig\":{\"size\":20,\"strokeStyle\":\"#dfe0e1\",\"lineWidth\":1}},\"elements\":[]}",
      }
    },
    watch: {
      currentType(newValue, oldValue) {
        this.Whiteboard.updateCurrentType(newValue)
      },
    },
    mounted() {
      this.init();
    },
    methods: {
      init(){
        var options = {
          container: this.$refs.box,
          drawType: this.currentType,
        }
        this.Whiteboard = new TinyWhiteboard(options);
        let storeData = localStorage.getItem('TINY_WHITEBOARD_DATA')
        storeData = this.value?this.value:this.jsonNull
        if (storeData) {
          storeData = JSON.parse(storeData)
          ;[['backgroundColor', ''],['strokeStyle', '#000000'],['fontFamily', '微软雅黑, Microsoft YaHei'],['dragStrokeStyle', '#666'], ['fillStyle', 'transparent'], ['fontSize', 18]].forEach((item) => {
            if (storeData.state[item[0]] === undefined) {
              storeData.state[item[0]] = item[1]
            }
          })
          this.currentZoom = parseInt(storeData.state.scale * 100)
          this.scroll.x = parseInt(storeData.state.scrollX)
          this.scroll.y = parseInt(storeData.state.scrollY)
          this.showGrid = storeData.state.showGrid
          this.readonly = storeData.state.readonly
          this.Whiteboard.setData(storeData)
        }
        // 监听app内部修改类型事件
        this.Whiteboard.on('currentTypeChange', type => {
          this.currentType = type
        })
        // 监听元素激活事件
        this.Whiteboard.on('activeElementChange', element => {
          if (this.activeElement) {
            this.activeElement.off('elementRotateChange', this.onElementRotateChange)
          }
          this.activeElement = element
          if (element) {
            let { style, rotate: elementRotate } = element
            this.lineWidth = style.lineWidth
            this.fontFamily = style.fontFamily
            this.fontSize = style.fontSize
            this.lineDash = style.lineDash
            this.globalAlpha = style.globalAlpha
            this.rotate = elementRotate
            element.on('elementRotateChange', this.onElementRotateChange)
            this.activeElementType = element.type
            this.activeElementStyle = element.style.strokeStyle
            this.activeElementFillStyle = element.style.fillStyle
          }
        })
        // 元素多选变化
        this.Whiteboard.on('multiSelectChange', elements => {
          this.selectedElements = elements
          this.hasSelectedElements = this.selectedElements.length > 0
        })
        // 缩放变化
        this.Whiteboard.on('zoomChange', scale => {
          this.currentZoom = parseInt(scale * 100)
        })
        // 监听前进后退事件
        this.Whiteboard.on('shuttle', (index, length) => {
          this.canUndo = index > 0
          this.canRedo = index < length - 1
        })
        // 监听数据变化
        this.Whiteboard.on('change', data => {
          this.showGrid = data.state.showGrid
          // localStorage.setItem('TINY_WHITEBOARD_DATA', JSON.stringify(data))
          let str = JSON.stringify(this.Whiteboard.exportJson())
          this.$emit("input",str)
        })
        // 监听滚动变化
        this.Whiteboard.on('scrollChange', (x, y) => {
          this.scroll.y = parseInt(y)
          this.scroll.x = parseInt(x)
        })

        if(this.readOnly){
          this.readonly = true
          this.Whiteboard.setReadonlyMode()
        }

        // 窗口尺寸变化
        let resizeTimer = null
        window.addEventListener('resize', () => {
          clearTimeout(resizeTimer)
          resizeTimer = setTimeout(() => {
            this.Whiteboard.resize()
          }, 300)
        })
      },
      // 元素角度变化
      onElementRotateChange(elementRotate) {
        this.rotate = elementRotate
      },
      // 修改元素角度
      onRotateChange(rotate) {
        this.Whiteboard.updateActiveElementRotate(rotate)
      },
      // 数字输入框聚焦事件
      onInputNumberFocus() {
        // 解绑快捷键按键事件，防止冲突
        this.Whiteboard.keyCommand.unBindEvent()
      },
      // 数字输入框失焦事件
      onInputNumberBlur() {
        // 重新绑定快捷键按键事件
        this.Whiteboard.keyCommand.bindEvent()
      },
      // 类型变化
      onCurrentTypeChange() {
        // 清除激活项
        this.Whiteboard.cancelActiveElement()
      },
      // 删除元素
      deleteElement() {
        this.Whiteboard.deleteCurrentElements()
      },
      // 复制元素
      copyElement() {
        this.Whiteboard.copyPasteCurrentElements()
      },
      // 更新样式
      updateStyle(key, value) {
        this.Whiteboard.setCurrentElementsStyle({
          [key]: value
        })
      },
      // 放大
      zoomIn() {
        this.Whiteboard.zoomIn()
      },
      // 缩小
      zoomOut() {
        this.Whiteboard.zoomOut()
      },
      // 恢复初始缩放
      resetZoom() {
        this.Whiteboard.setZoom(1)
      },
      // 橡皮擦
      toggleEraser() {
        this.currentType = this.currentType === 'eraser' ? 'selection' : 'eraser'
      },
      // 回退
      undo() {
        this.Whiteboard.undo()
      },
      // 前进
      redo() {
        this.Whiteboard.redo()
      },
      // 清空
      empty() {
        this.Whiteboard.empty()
      },
      // 更新背景颜色
      setBackgroundColor(value) {
        this.Whiteboard.setBackgroundColor(value)
      },
      // 滚动至中心
      scrollToCenter() {
        this.Whiteboard.scrollToCenter()
      },
      // 切换显示网格
      toggleGrid() {
        if (this.showGrid) {
          this.showGrid = false
          this.Whiteboard.hideGrid()
        } else {
          this.showGrid = true
          this.Whiteboard.showGrid()
        }
      },
      // 模式切换
      toggleMode() {
        if (this.readonly) {
          this.readonly = false
          this.Whiteboard.setEditMode()
        } else {
          this.readonly = true
          this.Whiteboard.setReadonlyMode()
        }
      },
      // 导入
      importFromJson() {
        let el = document.createElement('input')
        el.type = 'file'
        el.accept = 'application/json'
        el.addEventListener('input', () => {
          let reader = new FileReader()
          reader.onload = () => {
            el.value = null
            if (reader.result) {
              this.Whiteboard.setData(JSON.parse(reader.result))
            }
          }
          reader.readAsText(el.files[0])
        })
        el.click()
      },

      // 导出
      handleExportCommand(type) {
        if (type === 'png') {
          this.exportImageUrl = this.Whiteboard.exportImage({
            renderBg: this.exportRenderBackground,
            paddingX: this.exportImagePaddingX,
            paddingY: this.exportImagePaddingY,
            onlySelected: this.exportOnlySelected
          })
          this.exportImageDialogVisible = true
        } else if (type === 'json') {
          this.exportJsonData = this.Whiteboard.exportJson()
          this.exportJsonDialogVisible = true
          this.$nextTick(() => {
            if (!this.tree) {
              this.tree = jsonTree.jsonTree.create(this.exportJsonData, this.$refs.jsonPreviewBox)
            } else {
              this.tree.loadData(this.exportJsonData)
            }
          })
        }
      },
      // 重新生成导出图片
      reRenderExportImage() {
        this.exportImageUrl = this.Whiteboard.exportImage({
          renderBg: this.exportRenderBackground,
          paddingX: this.exportImagePaddingX,
          paddingY: this.exportImagePaddingY,
          onlySelected: this.exportOnlySelected
        })
      },
      // 下载导出的图片
      downloadExportImage() {
        TinyWhiteboard.utils.downloadFile(
          this.exportImageUrl,
          this.exportFileName + '.png'
        )
      },
      // 下载导出的json
      downloadExportJson() {
        let str = JSON.stringify(this.exportJsonData, null, 4)
        let blob = new Blob([str])
        TinyWhiteboard.utils.downloadFile(
          URL.createObjectURL(blob),
          this.exportFileName + '.json'
        )
      },
    },
  }
</script>
<style lang="scss">
  .exportJsonContainer {
     li {
       list-style-type:none;
     }
  }
</style>
<style>
  @import url("libs/jsonTree.css");
  .container {
    position: fixed;
    left: 0;
    top: 210px;
    width: 100%;
    height: calc(100vh - 230px);

    .toolbar {
      position: absolute;
      left: 50%;
      top: 10px;
      transform: translateX(-50%);
      z-index: 2;
      display: flex;
      justify-content: center;
    }

    .canvasBox {
      position: absolute;
      left: 50%;
      top: 50%;
      width: 100%;
      height: 100%;
      transform: translate(-50%, -50%);
      background-color: #fff;
    }

    .sidebar {
      position: absolute;
      left: 10px;
      top: 10px;
      width: 250px;
      background-color: #fff;

      .elementStyle {
        padding: 10px;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.25);
        border-radius: 4px;

        .styleBlock {
          margin-bottom: 10px;

          .styleBlockTitle {
            color: #343a40;
            font-size: 14px;
            margin-bottom: 10px;
          }

          .styleBlockContent {
            display: flex;

            .lineWidthItem {
              display: flex;
              width: 30px;
              height: 10px;
              align-items: center;

              .bar {
                width: 100%;
                background-color: #212529;
              }

              &.small {
                .bar {
                  height: 2px;
                }
              }

              &.middle {
                .bar {
                  height: 4px;
                }
              }

              &.large {
                .bar {
                  height: 6px;
                }
              }
            }

            /deep/ .el-radio-group {
              .el-radio-button {
                &.is-active {
                  .lineWidthItem {
                    .bar {
                      background-color: #fff;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    .TinyWhiteboard-footerLeft {
      position: absolute;
      left: 10px;
      bottom: 10px;
      height: 40px;
      display: flex;
      align-items: center;

      .blockBox {
        height: 100%;
        display: flex;
        align-items: center;
        padding: 0 10px;

        .zoom {
          width: 40px;
          margin: 0 10px;
          user-select: none;
          color: #606266;
          cursor: pointer;
          height: 32px;
          display: flex;
          align-items: center;
          background-color: #fff;
          border-radius: 5px;
          padding: 0 5px;
          justify-content: center;
        }
      }
    }
  }

  .exportImageContainer {
    .imagePreviewBox {
      height: 400px;
      background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAMUlEQVQ4T2NkYGAQYcAP3uCTZhw1gGGYhAGBZIA/nYDCgBDAm9BGDWAAJyRCgLaBCAAgXwixzAS0pgAAAABJRU5ErkJggg==')
        0;
      padding: 10px;
      overflow: auto;

      img {
        width: 100%;
        height: 100%;
        object-fit: scale-down;
      }
    }

    .handleBox {
      display: flex;
      align-items: center;
      height: 50px;
      justify-content: center;
    }
  }

  .exportJsonContainer {
    .jsonPreviewBox {
      height: 400px;
      overflow: auto;
      background-color: #f5f5f5;
      font-size: 14px;
      color: #000;

      /deep/ .jsontree_tree {
        font-family: 'Trebuchet MS', Arial, sans-serif !important;
      }
    }

    .handleBox {
      display: flex;
      align-items: center;
      height: 50px;
      justify-content: center;
    }
  }

  .helpDialogContent {
    height: 500px;
    overflow: auto;
  }
</style>
