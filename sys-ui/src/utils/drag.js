import Vue from 'vue'; // 引入vue

// 指令 v-dialogDrag: 弹窗拖拽
Vue.directive('drag', {
	bind(el, binding, vnode, oldVnode) {
		// 获取拖拽内容头部
		const dialogHeaderEl = el.querySelector('.el-dialog__header');
		const dragDom = el.querySelector('.el-dialog');
		dialogHeaderEl.style.cssText += ';cursor:move;';
		dragDom.style.cssText += ';top:0px;';

		// 获取dom原有属性
		const sty = (function () {
			if (window.document.currentStyle) {
				return (dom, attr) => dom.currentStyle[attr];
			} else {
				return (dom, attr) => getComputedStyle(dom, false)[attr];
			}
		})();
		// 获取原有属性 IE dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null)
		// const sty =
		// 	dragDom.currentStyle || window.getComputedStyle(dragDom, null);
		// 鼠标按下事件
		dialogHeaderEl.onmousedown = (e) => {
			if (e.preventDefault) {
				e.preventDefault();
			} else {
				e.returnValue = false; // 解决快速拖动滞后问题
			}
			// 鼠标按下，计算当前元素距离可视区的距离（鼠标点击位置距离可视窗口的距离）
			const disX = e.clientX - dialogHeaderEl.offsetLeft;
			const disY = e.clientY - dialogHeaderEl.offsetTop;

			const screenWidth = document.body.clientWidth; // body当前宽度
			const screenHeight = document.documentElement.clientHeight; // 可见区域高度(应为body高度，可某些环境下无法获取)

			const dragDomWidth = dragDom.offsetWidth; // 对话框宽度
			const dragDomheight = dragDom.offsetHeight; // 对话框高度

			const minDragDomLeft = dragDom.offsetLeft;
			const maxDragDomLeft =
				screenWidth - dragDom.offsetLeft - dragDomWidth;

			const minDragDomTop = dragDom.offsetTop;
			const maxDragDomTop =
				screenHeight - dragDom.offsetTop - dragDomheight;

			// 获取到的值带px 正则匹配替换
			let styL = sty(dragDom, 'left');
			let styT = sty(dragDom, 'top');

			// 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
			if (styL.includes('%')) {
				styL =
					+document.body.clientWidth *
					(+styL.replace(/\%/g, '') / 100);
				styT =
					+document.body.clientHeight *
					(+styT.replace(/\%/g, '') / 100);
			} else {
				styL = +styL.replace(/px/g, '');
				styT = +styT.replace(/px/g, '');
			}

			document.onmousemove = function (e) {
				// 通过事件委托，计算移动的距离
				// 鼠标的位置减去鼠标相对元素的位置，得到元素的位置
				let left = e.clientX - disX;
				let top = e.clientY - disY;
				if (minDragDomLeft <= 0) {
					left = 0;
				}

				// 边界处理
				if (-left > minDragDomLeft) {
					left = -minDragDomLeft;
				} else if (left > maxDragDomLeft) {
					left = maxDragDomLeft;
				}

				if (-top > minDragDomTop) {
					top = -minDragDomTop;
				} else if (top > maxDragDomTop) {
					top = maxDragDomTop;
				}

				// 移动当前元素
				dragDom.style.cssText += `;left:${left + styL}px;top:${
					top + styT
				}px;`;
			};

			document.onmouseup = function (e) {
				document.onmousemove = null;
				document.onmouseup = null;
			};
		};
	},
	update: function (el) {
		// console.log(el, 'update:el');
		// 当弹窗放大时， 保证弹窗移至边界处弹窗不会被遮挡
		const dragDom = el.querySelector('.el-dialog');
		dragDom.style.cssText += ';top:0px;left:0';
	},
});
