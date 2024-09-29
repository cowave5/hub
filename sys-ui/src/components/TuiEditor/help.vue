<template>
  <div :id="id" />
</template>

<script>
// deps for editor
import Editor from '@toast-ui/editor'
import "@toast-ui/editor/dist/i18n/zh-cn";
import '@toast-ui/editor/dist/toastui-editor.css' // Editor's Style

import { DOCS_HELP } from './help.js'

export default {
  name: 'HelpEditor',
  props: {
    id: {
      type: String,
      required: false,
      default() {
        return 'tui-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
      }
    },
    height: {
      type: String,
      required: false,
      default: '500px'
    },
  },
  data() {
    return {
      editor: null
    }
  },
  computed: {

  },
  watch: {
    height(newValue) {
      this.editor.height(newValue)
    },
  },
  mounted() {
    this.initEditor()
  },
  destroyed() {
    this.destroyEditor()
  },
  methods: {
    initEditor() {
      this.editor = Editor.factory({
        el: document.getElementById(this.id),
        viewer: true,
        height: this.height,
        initialValue: DOCS_HELP,
      })
    },
    destroyEditor() {
      if (!this.editor) return
      this.editor.off('change')
      this.editor.destroy()
    },
  }
}
</script>
