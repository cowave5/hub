import { mergeRecursive } from "@/utils/ruoyi";
import DictOptions from './DictOptions'

export default class DictMeta {
  constructor(options) {
    this.request = options.request
    this.responseConverter = options.responseConverter
    this.type = options.type
    this.codeField = options.codeField
    this.nameField = options.nameField
    this.valueField = options.valueField
    this.cssField = options.cssField
    this.lazy = options.lazy === true
  }
}

DictMeta.parse= function(options) {
  let opts = null
  if (typeof options === 'string') {
    opts = DictOptions.metas[options] || {}
    opts.type = options
  } else if (typeof options === 'object') {
    opts = options
  }
  opts = mergeRecursive(DictOptions.metas['*'], opts)
  return new DictMeta(opts)
}
