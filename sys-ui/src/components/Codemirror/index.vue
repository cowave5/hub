<template>
  <div class="vue-codemirror">
    <el-row>
      <el-col :span="language==='text/html'?12:24">
        <el-row>
          <el-col :span="24">
            <el-select size="mini" v-model="language">
              <el-option v-for="item in languages" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <el-select size="mini" v-model="cmTheme" style="margin-left: 10px;">
              <el-option v-for="item in cmThemes" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <el-button v-show="language==='text/html'" size="mini" style="float: right;margin-right: 5px;" type="success" plain @click="runBlank">运行至新页签</el-button>
          </el-col>
        </el-row>
        <el-row style="margin-top: 5px;">
          <el-col :span="24">
            <codemirror class="codemirrorEditor" id="editor" ref="editor" v-model="code" :options="options"
              @changes="onCmCodeChanges" @blur="onCmBlur" @keydown.native="onKeyDown" @mousedown.native="onMouseDown"
              @paste.native="OnPaste">
            </codemirror>
          </el-col>
        </el-row>
      </el-col>
      <el-col v-if="language==='text/html'" :span="12">
        <iframe class="codemirror-iframe" id="frame" frameborder="0"></iframe>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {
    codemirror
  } from "vue-codemirror";
  import 'codemirror/keymap/sublime'
  import "codemirror/mode/javascript/javascript.js";
  import "codemirror/mode/xml/xml.js";
  import "codemirror/mode/htmlmixed/htmlmixed.js";
  import "codemirror/mode/css/css.js";
  import 'codemirror/mode/vue/vue.js'
  import 'codemirror/mode/clike/clike.js'
  import 'codemirror/mode/r/r.js'
  import 'codemirror/mode/shell/shell.js'
  import 'codemirror/mode/swift/swift.js'
  import "codemirror/mode/yaml/yaml.js";
  import "codemirror/mode/sql/sql.js";
  import "codemirror/mode/python/python.js";
  import "codemirror/mode/markdown/markdown.js";
  import "codemirror/addon/hint/show-hint.css";
  import "codemirror/addon/hint/show-hint.js";
  import "codemirror/addon/hint/javascript-hint.js";
  import "codemirror/addon/hint/xml-hint.js";
  import "codemirror/addon/hint/css-hint.js";
  import "codemirror/addon/hint/html-hint.js";
  import "codemirror/addon/hint/sql-hint.js";
  import "codemirror/addon/hint/anyword-hint.js";
  import "codemirror/addon/lint/lint.css";
  import "codemirror/addon/lint/lint.js";
  import "codemirror/addon/lint/json-lint";
  import 'codemirror/addon/selection/active-line'
  import "codemirror/addon/hint/show-hint.js";
  import "codemirror/addon/hint/anyword-hint.js";
  require("script-loader!jsonlint");
  import "codemirror/addon/lint/javascript-lint.js";
  import "codemirror/addon/fold/foldcode.js";
  import "codemirror/addon/fold/foldgutter.js";
  import "codemirror/addon/fold/foldgutter.css";
  import "codemirror/addon/fold/brace-fold.js";
  import "codemirror/addon/fold/xml-fold.js";
  import "codemirror/addon/fold/comment-fold.js";
  import "codemirror/addon/fold/markdown-fold.js";
  import "codemirror/addon/fold/indent-fold.js";
  import "codemirror/addon/edit/closebrackets.js";
  import "codemirror/addon/edit/closetag.js";
  import "codemirror/addon/edit/matchtags.js";
  import "codemirror/addon/edit/matchbrackets.js";
  import "codemirror/addon/selection/active-line.js";
  import "codemirror/addon/search/jump-to-line.js";
  import "codemirror/addon/dialog/dialog.js";
  import "codemirror/addon/dialog/dialog.css";
  import "codemirror/addon/search/searchcursor.js";
  import "codemirror/addon/search/search.js";
  import "codemirror/addon/display/autorefresh.js";
  import "codemirror/addon/selection/mark-selection.js";
  import "codemirror/addon/search/match-highlighter.js";
  // 编辑的主题文件
  import './theme.js'

  export default {
    name: 'codeEditor',
    /* eslint-disable vue/require-prop-types */
    props: ['value'],
    components: {
      codemirror
    },
    data() {
      return {
        code: this.value, // 编辑器绑定的值
        // 默认配置
        options: {
          theme: 'panda-syntax', // 主题
          mode: 'text/html', // 代码格式
          tabSize: 4, // tab的空格个数
          indentUnit: 2, // 一个块（编辑语言中的含义）应缩进多少个空格
          autocorrect: true, // 自动更正
          spellcheck: true, // 拼写检查
          lint: true, // 检查格式
          lineNumbers: true, //是否显示行数
          lineWrapping: true, //是否自动换行
          styleActiveLine: true, //line选择是是否高亮
          keyMap: 'sublime', // sublime编辑器效果
          matchBrackets: true, //括号匹配
          autoCloseBrackets: true, // 在键入时将自动关闭括号和引号
          matchTags: {
            bothTags: true
          }, // 将突出显示光标周围的标签
          foldGutter: true, // 可将对象折叠，与下面的gutters一起使用
          gutters: [
            "CodeMirror-lint-markers",
            "CodeMirror-linenumbers",
            "CodeMirror-foldgutter"
          ],
          highlightSelectionMatches: {
            minChars: 2,
            style: "matchhighlight",
            showToken: true
          },
        },
        enableAutoFormatJson: true, // json编辑模式下，输入框失去焦点时是否自动格式化，true 开启， false 关闭


        languages: [{
          value: 'text/html',
          label: 'html'
        }, {
          value: 'application/json',
          label: 'json'
        }, {
          value: 'text/css',
          label: 'CSS'
        }, {
          value: 'text/javascript',
          label: 'Javascript'
        }, {
          value: 'text/x-java',
          label: 'Java'
        }, {
          value: 'text/x-objectivec',
          label: 'Objective-C'
        }, {
          value: 'text/x-python',
          label: 'Python'
        }, {
          value: 'text/x-rsrc',
          label: 'R'
        }, {
          value: 'text/x-sh',
          label: 'Shell'
        }, {
          value: 'text/x-sql',
          label: 'SQL'
        }, {
          value: 'text/x-swift',
          label: 'Swift'
        }, {
          value: 'text/x-vue',
          label: 'Vue'
        }, {
          value: 'text/markdown',
          label: 'Markdown'
        }],
        language: 'text/html',
        cmThemes: [{
          value: '3024-day',
          label: ''
        }, {
          value: '3024-night',
          label: ''
        }, {
          value: 'abcdef',
          label: ''
        }, {
          value: 'ambiance-mobile',
          label: ''
        }, {
          value: 'ambiance',
          label: ''
        }, {
          value: 'ayu-dark',
          label: ''
        }, {
          value: 'ayu-mirage',
          label: ''
        }, {
          value: 'base16-dark',
          label: ''
        }, {
          value: 'base16-light',
          label: ''
        }, {
          value: 'bespin',
          label: ''
        }, {
          value: 'blackboard',
          label: ''
        }, {
          value: 'cobalt',
          label: ''
        }, {
          value: 'colorforth',
          label: ''
        }, {
          value: 'darcula',
          label: ''
        }, {
          value: 'dracula',
          label: ''
        }, {
          value: 'duotone-dark',
          label: ''
        }, {
          value: 'duotone-light',
          label: ''
        }, {
          value: 'eclipse',
          label: ''
        }, {
          value: 'elegant',
          label: ''
        }, {
          value: 'erlang-dark',
          label: ''
        }, {
          value: 'gruvbox-dark',
          label: ''
        }, {
          value: 'hopscotch',
          label: ''
        }, {
          value: 'icecoder',
          label: ''
        }, {
          value: 'idea',
          label: ''
        }, {
          value: 'isotope',
          label: ''
        }, {
          value: 'lesser-dark',
          label: ''
        }, {
          value: 'liquibyte',
          label: ''
        }, {
          value: 'lucario',
          label: ''
        }, {
          value: 'material-darker',
          label: ''
        }, {
          value: 'material-ocean',
          label: ''
        }, {
          value: 'material-palenight',
          label: ''
        }, {
          value: 'material',
          label: ''
        }, {
          value: 'mbo',
          label: ''
        }, {
          value: 'mdn-like',
          label: ''
        }, {
          value: 'midnight',
          label: ''
        }, {
          value: 'monokai',
          label: ''
        }, {
          value: 'moxer',
          label: ''
        }, {
          value: 'neat',
          label: ''
        }, {
          value: 'neo',
          label: ''
        }, {
          value: 'night',
          label: ''
        }, {
          value: 'nord',
          label: ''
        }, {
          value: 'oceanic-next',
          label: ''
        }, {
          value: 'panda-syntax',
          label: ''
        }, {
          value: 'paraiso-dark',
          label: ''
        }, {
          value: 'paraiso-light',
          label: ''
        }, {
          value: 'pastel-on-dark',
          label: ''
        }, {
          value: 'rubyblue',
          label: ''
        }, {
          value: 'seti',
          label: ''
        }, {
          value: 'shadowfox',
          label: ''
        }, {
          value: 'solarized',
          label: ''
        }, {
          value: 'ssms',
          label: ''
        }, {
          value: 'the-matrix',
          label: ''
        }, {
          value: 'tomorrow-night-bright',
          label: ''
        }, {
          value: 'tomorrow-night-eighties',
          label: ''
        }, {
          value: 'ttcn',
          label: ''
        }, {
          value: 'twilight',
          label: ''
        }, {
          value: 'vibrant-ink',
          label: ''
        }, {
          value: 'xq-dark',
          label: ''
        }, {
          value: 'xq-light',
          label: ''
        }, {
          value: 'yeti',
          label: ''
        }, {
          value: 'yonce',
          label: ''
        }],
        cmTheme: 'panda-syntax',
      }
    },
    watch: {
      code(value) {
        this.$emit('input', value)
        if(this.language === 'text/html') {
          localStorage.setItem('REVEALDATA', value)
          const frame = document.querySelector('#frame')
          frame.contentWindow.document.open()
          frame.contentWindow.document.write(value)
        }
      },
      language(value) {
        if (value) {
          this.$nextTick(() => {
            this.$refs.editor.codemirror.setOption("mode", value);
            if(this.language === 'text/html') {
              localStorage.setItem('REVEALDATA', this.code)
              const frame = document.querySelector('#frame')
              frame.contentWindow.document.open()
              frame.contentWindow.document.write(this.code!==null?this.code:'')
            }
          });
        }
      },
      cmTheme(value) {
        if (value) {
          this.$nextTick(() => {
            this.$refs.editor.codemirror.setOption("theme", value);
          });
        }
      },
    },
    created() {
      try {
        if (!this.code) {
          this.options.lint = false;
          return;
        }
        if (this.options.mode === "application/json") {
          if (!this.enableAutoFormatJson) {
            return;
          }
          this.code = this.formatStrInJson(this.code);
        }
      } catch (e) {
        console.log("初始化codemirror出错：" + e);
      }
    },
    mounted() {
      if(this.language === 'text/html') {
        localStorage.setItem('REVEALDATA', this.code)
        const frame = document.querySelector('#frame')
        frame.contentWindow.document.open()
        frame.contentWindow.document.write(this.code!==null?this.code:'')
      }
    },
    beforeDestroy() {
      localStorage.setItem('REVEALDATA', null)
    },
    methods: {
      resetLint() {
        if (!this.$refs.editor.codemirror.getValue()) {
          this.$nextTick(() => {
            this.$refs.editor.codemirror.setOption("lint", false);
          });
          return;
        }
        this.$refs.editor.codemirror.setOption("lint", false);
        this.$nextTick(() => {
          this.$refs.editor.codemirror.setOption("lint", true);
        });
      },
      // 格式化字符串为json格式字符串
      formatStrInJson(strValue) {
        return JSON.stringify(
          JSON.parse(strValue),
          null,
          this.cmIndentUnit
        );
      },
      onCmCodeChanges(cm, changes) {
        this.code = cm.getValue();
        this.resetLint();
      },
      // 失去焦点时处理函数
      onCmBlur(cm, event) {
        try {
          let code = cm.getValue();
          if (this.options.mode === "application/json" && code) {
            if (!this.enableAutoFormatJson) {
              return;
            }
            this.code = this.formatStrInJson(code);
          }
        } catch (e) {
          // 啥也不做
        }
      },
      // 按下键盘事件处理函数
      onKeyDown(event) {
        const keyCode = event.keyCode || event.which || event.charCode;
        const keyCombination =
          event.ctrlKey || event.altKey || event.metaKey;
        if (!keyCombination && keyCode > 64 && keyCode < 123) {
          this.$refs.editor.codemirror.showHint({
            completeSingle: false
          });
        }
      },
      // 按下鼠标时事件处理函数
      onMouseDown(event) {
        this.$refs.editor.codemirror.closeHint();
      },
      // 黏贴事件处理函数
      OnPaste(event) {
        if (this.options.mode === "application/json") {
          try {
            this.code = this.formatStrInJson(this.code);
          } catch (e) {
            // 啥都不做
          }
        }
      },
      runBlank(){
        let routeData = this.$router.resolve({
          name: 'reveal', //注意使用 params 时一定不能使用 path
        })
        window.open(routeData.href, '_blank')
      },
    }
  }
</script>

<style lang="scss">
  .vue-codemirror {
    height: calc(100vh - 260px);

    .CodeMirror {
      height: calc(100vh - 250px) !important;
    }

    .codemirror-iframe {
      width: 100vh;
      height: calc(100vh - 230px);
    }
  }
</style>
