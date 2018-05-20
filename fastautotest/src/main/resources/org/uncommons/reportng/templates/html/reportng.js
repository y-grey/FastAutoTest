function toggleElement(elementId, displayStyle) {
	var element = document.getElementById(elementId);
	var current = element.currentStyle ?
		element.currentStyle['display'] :
		document.defaultView.getComputedStyle(element, null).getPropertyValue('display');
	element.style.display = (current == 'none' ? displayStyle : 'none');
}

function toggle(toggleId) {
	var toggle = document.getElementById ? document.getElementById(toggleId) : document.all[toggleId];
	toggle.textContent = toggle.innerHTML == '\u25b6' ? '\u25bc' : '\u25b6';
}

function imgShow(outerdiv, innerdiv, bigimg, _this) {
	var src = _this.attr("src"); //获取当前点击的pimg元素中的src属性  
	$(bigimg).attr("src", src); //设置#bigimg元素的src属性  

	/*获取当前点击图片的真实大小，并显示弹出层及大图*/
	$("<img/>").attr("src", src).load(function() {
		var windowW = $(window).width() > window.screen.availWidth ? document.body.clientWidth : $(window).width(); //获取当前窗口宽度  
		var windowH = $(window).height() > window.screen.availHeight ? document.body.clientHeight : $(window).height(); //获取当前窗口高度  
		var realWidth = this.width; //获取图片真实宽度  
		var realHeight = this.height; //获取图片真实高度  
		var imgWidth, imgHeight;
		var scale = 1; //缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

		if(realHeight > windowH * scale) { //判断图片高度  
			imgHeight = windowH * scale; //如大于窗口高度，图片高度进行缩放  
			imgWidth = imgHeight / realHeight * realWidth; //等比例缩放宽度  
			if(imgWidth > windowW * scale) { //如宽度扔大于窗口宽度  
				imgWidth = windowW * scale; //再对宽度进行缩放  
			}
		} else if(realWidth > windowW * scale) { //如图片高度合适，判断图片宽度  
			imgWidth = windowW * scale; //如大于窗口宽度，图片宽度进行缩放  
			imgHeight = imgWidth / realWidth * realHeight; //等比例缩放高度  
		} else { //如果图片真实高度和宽度都符合要求，高宽不变  
			imgWidth = realWidth;
			imgHeight = realHeight;
		}
		$(bigimg).css("width", imgWidth); //以最终的宽度对图片缩放  

		var w = (windowW - imgWidth) / 2; //计算图片与窗口左边距  
		var h = (windowH - imgHeight) / 2; //计算图片与窗口上边距  
		$(innerdiv).css({
			"top": h,
			"left": w
		}); //设置#innerdiv的top和left属性  
		$(outerdiv).fadeIn("fast"); //淡入显示#outerdiv及.pimg  
	});

	$(outerdiv).click(function() { //再次点击淡出消失弹出层  
		$(this).fadeOut("fast");
	});
}

function showCircleChart(render, title) {
	pcount = document.getElementById(title + "tpn").innerHTML;
	fcount = document.getElementById(title + "tfn").innerHTML;
	scount = document.getElementById(title + "tsn").innerHTML;
	var chart = iChart.create({
		render: render,
		width: 340,
		height: 260,
		background_color: "#fefefe",
		gradient: false,
		color_factor: 0.2,
		border: {
			color: "BCBCBC",
			width: 0
		},
		align: "top|center",
		offsetx: 0,
		offsety: 0,
		sub_option: {
			border: {
				color: "#BCBCBC",
				width: 1
			},
			label: {
				fontweight: 500,
				fontsize: 11,
				color: "#4572a7",
				sign: "square",
				sign_size: 12,
				border: {
					color: "#BCBCBC",
					width: 1
				}
			}
		},
		shadow: true,
		shadow_color: "#666666",
		shadow_blur: 2,
		showpercent: false,
		column_width: "70%",
		bar_height: "70%",
		radius: "90%",
		title: {
			text: title,
			color: '#3e576f'
		},
		subtitle: {
			text: "",
			color: "#111111",
			fontsize: 16,
			font: "微软雅黑",
			textAlign: "center",
			height: 20,
			offsetx: 0,
			offsety: 0
		},
		footnote: {
			text: "",
			color: "#111111",
			fontsize: 12,
			font: "微软雅黑",
			textAlign: "right",
			height: 20,
			offsetx: 0,
			offsety: 0
		},
		legend: {
			enable: false,
			background_color: "#fefefe",
			color: "#333333",
			fontsize: 12,
			border: {
				color: "#BCBCBC",
				width: 1
			},
			column: 1,
			align: "right",
			valign: "center",
			offsetx: 0,
			offsety: 0
		},
		coordinate: {
			width: "80%",
			height: "84%",
			background_color: "#ffffff",
			axis: {
				color: "#a5acb8",
				width: [1, "", 1, ""]
			},
			grid_color: "#d9d9d9",
			label: {
				fontweight: 500,
				color: "#666666",
				fontsize: 11
			}
		},
		label: {
			fontweight: 500,
			color: "#666666",
			fontsize: 11
		},
		type: "pie2d",
		data: [{
			name: "Passed",
			value: pcount,
			color: "#44aa44"
		}, {
			name: "Failed",
			value: fcount,
			color: "#ff4444"
		}, {
			name: "Skipped",
			value: scount,
			color: "#FFD700"
		}]
	});
	chart.draw();
}

function showLineChart(render, title, subtitle,unit, value ,stack) {
	var data = [{
					name: 'PV',
					value: value,
					color: '#ec4646',
					line_width: 2
				}];
	var chartWidth;
	if(value.length<100){
		chartWidth = 1000
	}else{
		chartWidth = value.length*10
	}
	var coordinateWidth = chartWidth-60;
	var chart = new iChart.LineBasic2D({
		render: render,
		data: data,
		align: 'right',
		title: {
			text: title,
			font: '微软雅黑',
			fontsize: 24,
			color: '#b4b4b4'
		},
		subtitle : {
        	text:subtitle,
        	font : '微软雅黑',
        	fontsize: 12,
        	color:'#b4b4b4'
        				},
		width: chartWidth,
		height: 330,
		shadow: true,
		shadow_color: '#202020',
		shadow_blur: 8,
		shadow_offsetx: 0,
		shadow_offsety: 0,
		background_color: '#2e2e2e',
		tip: {
			enable: true,
			shadow: true,
			listeners: {
				//tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
				parseText: function(tip, name, value, text, i) {
					return "<span style='color:#005268;font-size:12px;'>"+stack[i]+"<br/>";
				}
			}
		},
		crosshair: {
			enable: true,
			line_color: '#ec4646'
		},
		sub_option: {
			smooth: true,
			label: false,
			hollow: false,
			hollow_inside: false,
			point_size: 8
		},
		coordinate: {
			width: coordinateWidth,
			height: 240,
			striped_factor: 0.18,
			grid_color: '#4e4e4e',
			axis: {
				color: '#252525',
				width: [0, 0, 4, 4]
			},
			scale: [{
				position: 'left',
				start_scale: 0,
				end_scale: 100,
				scale_space: 10,
				scale_size: 2,
				scale_enable: false,
				label: {
					color: '#9d987a',
					font: '微软雅黑',
					fontsize: 11,
					fontweight: 600
				},
				scale_color: '#9f9f9f'
			}]
		}
	});
	chart.plugin(new iChart.Custom({
		drawFn: function() {
			//计算位置
			var coo = chart.getCoordinate(),
				x = coo.get('originx'),
				y = coo.get('originy'),
				w = coo.width,
				h = coo.height;
			//在左上侧的位置，渲染一个单位的文字
			chart.target.textAlign('start')
				.textFont('600 11px 微软雅黑')
				.fillText(unit, x - 40, y - 12, false, '#9d987a')
				.textBaseline('top');

		}
	}));
	//开始画图
	chart.draw();
}
