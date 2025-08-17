import { mergeRecursive } from "@/utils/ruoyi";
import dictConverter from './DictConverter'

export const options = {
  metas: {
    '*': {
      /**
       * 字典请求，方法签名为function(dictMeta: DictMeta): Promise
       */
      request: (dictMeta) => {
        return Promise.resolve([])
      },
      /**
       * 字典响应数据转换器，方法签名为function(response: Object, dictMeta: DictMeta): DictData
       */
      responseConverter,
      codeField: 'dictCode',
      nameField: 'dictName',
      valueField: 'dictValue',
      cssField: 'css'
    },
  },

  DEFAULT_CODE_FIELDS: ['code'],
  /**
   * 默认标签字段
   */
  DEFAULT_NAME_FIELDS: ['name'],
  /**
   * 默认值字段
   */
  DEFAULT_VALUE_FIELDS: ['value'],
  /**
   * 默认值字段
   */
  DEFAULT_CSS_FIELDS: ['css'],
}

/**
 * 映射字典
 * @param {Object} response 字典数据
 * @param {DictMeta} dictMeta 字典元数据
 * @returns {DictData}
 */
function responseConverter(response, dictMeta) {
  const dicts = response.list instanceof Array ? response.list : response
  if (dicts === undefined) {
    console.warn(`no dict data of "${dictMeta.type}"`)
    return []
  }
  return dicts.map(d => dictConverter(d, dictMeta))
}

export function mergeOptions(src) {
  mergeRecursive(options, src)
}

export default options
