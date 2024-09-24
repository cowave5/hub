import request from '@/utils/request'

// 流程选项
export function optionsDeploy() {
  return request({
    url: '/admin/api/v1/flow/deploy/options',
    method: 'get'
  })
}

// 部署列表
export function listDeploy(data) {
  return request({
    url: '/admin/api/v1/flow/deploy/list',
    method: 'post',
    data: data
  })
}

// 流程定义
export function showDefinition(deploymentId, resourceName) {
  return request({
    url: '/admin/api/v1/flow/deploy/definition?deploymentId=' + deploymentId + '&resourceName=' + resourceName,
    method: 'get'
  })
}

// 流程图
export function showDiagram(id) {
  return request({
    url: '/admin/api/v1/flow/deploy/diagram/' + id,
    method: 'get'
  })
}

// 转为模型
export function translateDeploy(id) {
  return request({
    url: '/admin/api/v1/flow/deploy/translate/' + id,
    method: 'get'
  })
}

// 删除部署
export function deleteDeploy(deploymentId) {
  return request({
    url: '/admin/api/v1/flow/deploy/delete/' + deploymentId,
    method: 'get'
  })
}
