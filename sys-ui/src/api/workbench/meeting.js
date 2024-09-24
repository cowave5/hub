import request from '@/utils/request'

// 会议列表
export function listMeeting(data) {
  return request({
    url: '/admin/api/v1/flow/meeting/list',
    method: 'post',
    data: data
  })
}

// 新增会议
export function addMeeting(data) {
  return request({
    url: '/admin/api/v1/flow/meeting/add',
    method: 'post',
    data: data
  })
}

// 修改会议
export function editMeeting(data) {
  return request({
    url: '/admin/api/v1/flow/meeting/edit',
    method: 'post',
    data: data
  })
}

// 会议详情
export function infoMeeting(id) {
  return request({
    url: '/admin/api/v1/flow/meeting/info/' + id,
    method: 'get'
  })
}

// 删除会议
export function delMeeting(ids) {
  return request({
    url: '/admin/api/v1/flow/meeting/delete/' + ids,
    method: 'get'
  })
}
