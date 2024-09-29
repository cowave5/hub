<template>
  <div>
    <div class="TuiEditor" :id="id" />
    <el-dialog class="TuiEditorHelp" title="帮助" :visible.sync="openHelp" width="800px" lock-scroll append-to-body>
      <help></help>
    </el-dialog>
  </div>
</template>

<script>
// deps for editor
import Editor from '@toast-ui/editor'
import "@toast-ui/editor/dist/i18n/zh-cn";
import '@toast-ui/editor/dist/toastui-editor.css' // Editor's Style
import defaultOptions from './default-options'

import help from './help.vue'

export default {
  name: 'TuiEditor',
  components: {
    help,
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      required: false,
      default() {
        return 'tui-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
      }
    },
    options: {
      type: Object,
      default() {
        return defaultOptions
      }
    },
    mode: {
      type: String,
      default: 'markdown'
    },
    height: {
      type: String,
      required: false,
      default: '300px'
    },
    language: {
      type: String,
      required: false,
      default: 'zh-CN' // https://github.com/nhnent/tui.editor/tree/master/src/js/langs
    }
  },
  data() {
    return {
      editor: null,
      toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        // ['hr', 'quote'],
        ['ul', 'ol'],
        // ['task', 'indent', 'outdent', 'table'],
        ['image', 'link'],
        // ['code', 'codeblock'],
        [{
          el: this.createLastButton(),
          command: 'slidePreview',
          tooltip: ''
        },{
          el: this.createHelpButton(),
          command: 'help',
          tooltip: '帮助'
        }],
      ],
      openHelp: false,
    }
  },
  computed: {
    editorOptions() {
      const options = Object.assign({}, defaultOptions, this.options)
      options.initialEditType = this.mode
      options.height = this.height
      options.language = this.language
      options.toolbarItems = this.toolbarItems
      options.hideModeSwitch = true // 隐藏模式切换选项卡栏
      options.previewStyle = 'tab'
      return options
    }
  },
  watch: {
    value(newValue, preValue) {
      if (newValue !== preValue && newValue !== this.editor.getMarkdown()) {
        this.editor.setMarkdown(newValue)
      }
    },
    language(val) {
      this.destroyEditor()
      this.initEditor()
    },
    height(newValue) {
      this.editor.setHeight(newValue)
    },
    mode(newValue) {
      this.editor.changeMode(newValue)
    }
  },
  mounted() {
    this.initEditor()
  },
  destroyed() {
    this.destroyEditor()
  },
  methods: {
    initEditor() {
      this.editor = new Editor({
        el: document.getElementById(this.id),
        ...this.editorOptions
      })
      if (this.value) {
        this.editor.setMarkdown(this.value)
        localStorage.setItem('MAEK_DISPLAY', this.value)
      }
      this.editor.on('change', () => {
        this.$emit('input', this.editor.getMarkdown())
        localStorage.setItem('MAEK_DISPLAY', this.editor.getMarkdown())
      })
      this.editor.insertToolbarItem({ groupIndex: 2, itemIndex: 3 }, {
        name: 'myItem',
        tooltip: '分割幻灯片',
        command: 'hr',
        // text: '🈹',
        className: 'hrline toastui-editor-toolbar-icons',
        // style: { backgroundImage: 'none' }
      });
    },
    destroyEditor() {
      if (!this.editor) return
      this.editor.off('change')
      this.editor.destroy()
      localStorage.setItem('MAEK_DISPLAY', null)
    },
    setValue(value) {
      this.editor.setMarkdown(value)
    },
    getValue() {
      return this.editor.getMarkdown()
    },
    setHtml(value) {
      this.editor.setHTML(value)
    },
    getHtml() {
      return this.editor.getHTML()
    },
    createLastButton() {
      const button = document.createElement('button');

      button.className = 'slidePreview-toolbar-icons';
      button.style.backgroundImage = 'none';
      button.style.margin = '0';
      button.style.width = '30vh';
      // button.style.backgroundColor = '#f7f9fc';
      button.innerHTML = `<b>预览幻灯片</b>`;
      button.addEventListener('click', () => {
        let routeData = this.$router.resolve({
          name: 'markDisplay', //注意使用 params 时一定不能使用 path
        })
        window.open(routeData.href, '_blank')
        // this.editor.exec('bold');
      });

      return button;
    },
    createHelpButton() {
      const button = document.createElement('button');

      button.className = 'slidePreview-toolbar-icons';
      button.style.backgroundImage = 'none';
      button.style.margin = '0';
      button.style.backgroundColor = '#f7f9fc';
      button.innerHTML = `❔️`;
      button.addEventListener('click', () => {
        this.openHelp = true
      });

      return button;
    }
  }
}
</script>
<style lang="scss">
  .TuiEditor {
    .tab-item {
      display:none!important;
    }
  }
  .TuiEditorHelp {
    .el-dialog__body {
      padding: 0 20px 30px 20px!important;
    }
  }
</style>
