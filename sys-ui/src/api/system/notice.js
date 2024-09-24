import request from '@/utils/request'

// 查询公告列表
export function listNotice(data) {
  return request({
    url: '/admin/api/v1/notice/list',
    method: 'post',
    data: data
  })
}

// 查询公告详细
export function getNotice(noticeId) {
  return request({
    url: '/admin/api/v1/notice/info/' + noticeId,
    method: 'get'
  })
}

// 已读列表
export function getReads(params) {
  return request({
    url: '/admin/api/v1/notice/read/list',
    method: 'get',
    params: params
  })
}

// 消息列表
export function getMsgs(params) {
  return request({
    url: '/admin/api/v1/notice/msg/list',
    method: 'get',
    params: params
  })
}

// 阅读消息
export function msgRead(noticeId) {
  return request({
    url: '/admin/api/v1/notice/msg/read/' + noticeId,
    method: 'get'
  })
}

// 反馈意见
export function msgBack(noticeId, readBack) {
  return request({
    url: '/admin/api/v1/notice/msg/back?noticeId=' + noticeId + '&readBack=' + readBack,
    method: 'get'
  })
}

// 新增公告
export function addNotice(data) {
  return request({
    url: '/admin/api/v1/notice/add',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateNotice(data) {
  return request({
    url: '/admin/api/v1/notice/edit',
    method: 'post',
    data: data
  })
}

// 删除公告
export function delNotice(noticeId) {
  return request({
    url: '/admin/api/v1/notice/delete?noticeId=' + noticeId,
    method: 'get'
  })
}

// 发布公告
export function publishNotice(noticeId) {
  return request({
    url: '/admin/api/v1/notice/publish/' + noticeId,
    method: 'get'
  })
}

export function uploadAttach(data, attachUrl) {
  return request({
    url: attachUrl,
    method: 'post',
    data: data
  })
}
