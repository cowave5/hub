import DictOptions from './DictOptions'
import DictData from './DictData'

export default function(dict, dictMeta) {
  const code = determineDictField(dict, dictMeta.codeField, ...DictOptions.DEFAULT_CODE_FIELDS)
  const name = determineDictField(dict, dictMeta.nameField, ...DictOptions.DEFAULT_NAME_FIELDS)
  const value = determineDictField(dict, dictMeta.valueField, ...DictOptions.DEFAULT_VALUE_FIELDS)
  const css = determineDictField(dict, dictMeta.cssField, ...DictOptions.DEFAULT_CSS_FIELDS)
  return new DictData(dict[code], dict[name], dict[value], dict[css], dict)
}

function determineDictField(dict, ...fields) {
  return fields.find(f => Object.prototype.hasOwnProperty.call(dict, f))
}
