/** 
 * @fileOverview 我的淘宝的基础模块, 主要用于页头导航, 搜索, 以及侧边栏的功能的
 * 初始化, 不依赖 SNS 1.1, 同时应避免对过多模块的依赖, 因为会在合作方的页面中被
 * 调用, 复杂的环境中, 应尽量保持无依赖或少依赖, 功能单一
 *
 * @author: 龙刚 <tblonggang@gmail.com>
 */

(function () {
    var S = KISSY,
            DOM = S.DOM,
            Event = S.Event;

    // IE 中兼容 html5 标签, 单独放在最前, 保证
    // 这块不会因为其他的脚本报错而使得页面大乱
    if (S.UA.ie) {
        var tagNames = ['header', 'firgure', 'article', 'section', 'aside', 'footer', 'nav'];
        S.each(tagNames, function (el) {
            document.createElement(el);
        });
    }

    // 设置 domain 为顶级域, 以实现跨子域的通信
    try {
        // 如果 host 为 xxxdaily.daily.taobao.net, 或者 daily.taobao.com 之
        // 类的, 可能会引起报错所以用 try catch 包围
        if (DOM.get('#J_ITouchDomain') || DOM.get('#J_quanzi')){
            document.domain = location.host.indexOf('daily') > -1 ?
                'taobao.net' : 'taobao.com';
        }
    } catch (err){}

    // MyTaobao 命名空间
    S.namespace('MTB', true);

    MTB = {
        _version: '3.0',
        _description: 'MyTaobao Namespace'
    }

	//MyTaobao页面配置	
	var mtConfig = {
		 _getSearchURL: function (type){
			switch (type) {
				case 'shop':
					return 'http://shopsearch.taobao.com/search?q=%CB%B9%B5%D9%B7%D2i&initiative_id=staobaoz_20120831&spm=a1z02.1.6856637.d4910789&stat=40&shopf=newsearch';
				case 'item':
					return 'http://s.taobao.com/search?q=%CB%B9%B5%D9%B7%D2i&spm=a1z02.1.6856637.d4910789';
			}
		},
		_searchForm: function (){
			return DOM.get('#J_TSearchForm')
		}
	}

    var mtBase = {
        _description: 'Mytaobao Base Module',
		//搜索类型变量存储,默认为宝贝
		_searchType: 'item',

        // 选中左侧菜单
        selectSideMenuItem: function (id) {
            // 历史问题, 选中左侧菜单的功能需要用JS来实现,
            // 因为改模板需要牵涉到多个应用方, 短时间较难立
            // 刻协调完修改工作
            var item, foldParentNode;

            item = DOM.get('#' + id);
            if (!item) {
                return;
            }

            // 将 item 定位到相应的 A 标签上
            if (item.nodeName.toUpperCase() !== 'A') {
                item = DOM.get('A', item);
            }

            DOM.addClass(item, 'selected');

            // 判断是否是在折叠菜单里面
            foldParentNode = DOM.parent(item, '.J_MtSideTree');
            if (!foldParentNode) {
                return
            }

            DOM.replaceClass(foldParentNode, 'fold', 'unfold');
        },

        init: function () {
            var host = this;

            // 存放这个方法到全局, 供历史页面调用
            window.selectItem = host.selectSideMenuItem;

            // 初始化 Mytaobao 的公告条
            S.available('#J_MtNotice', function (){
                host._initNotice();
            });

            // 初始化左侧的头像
            S.available('#J_MtSideMenu', function () {
                host._initAvatar();
            });

            // 不重要的东西, 则放到最后再去请求和添加事件
            S.ready(function () {
				//左侧导航我的圈子弹出层
                if(DOM.get('#J_quanzi')){
				host.myQuanziPop();
//                host._loadQzData().initCountData();//导航左侧我的圈子异步加载更新数
                }
                // 处理导航 hover 效果
                var mainNavItem = DOM.query('LI', DOM.get('#J_MtMainNav'));
				host.fixHover(mainNavItem, 'hover');
				host._initNavMenu();
				host._initSearch();
				host._initMenu();
				host._initRecentApp();
				host._initRecommentApp();
            });
        },

        fixHover: function (els, clsName) {
            if (!els || !S.UA.ie) {
                return;
            }

            /*S.each(els, function (el) {
                Event.on(el, 'mouseenter', function () {
                    DOM.addClass(el, clsName);
                });
                Event.on(el, 'mouseleave', function () {
                    DOM.removeClass(el, clsName);
                });
            });*/
        },

        // 初始顶部导航
        _initNavMenu: function () {
            var navContainer = DOM.get('#J_MtMainNav'),
                    triggers = DOM.query('.J_MtNavSubTrigger', navContainer),
                    panels = DOM.query('.J_MtNavSub', navContainer),
                    timer = null,
                    HIDE = 'hide',
                    HOVER = 'hover',
					WRAPHOVER = 'mt-nav-sub-wrap',
                    duration = 200,
					enterTimer = null,
					enterDuration = 200;

            /*if (S.UA.ie) {
                S.each(triggers, function (el) {
                    var triggerParent = DOM.parent(el, 'LI');

                    Event.on([el, triggerParent], 'mouseenter', function () {
                        DOM.addClass(this, HOVER);
                    });

                    Event.on([el, triggerParent], 'mouseleave', function () {
                        DOM.removeClass(this, HOVER);
                    });
                });
            }*/

            function _clearTimer() {
                timer && timer.cancel && timer.cancel();
            }
            function _clearEnterTimer() {
                enterTimer && enterTimer.cancel && enterTimer.cancel();
            }

            Event.on(triggers, 'mouseenter', function (ev) {
                var panel = DOM.children(ev.currentTarget, '.J_MtNavSub');

				DOM.removeClass(triggers, WRAPHOVER);
				DOM.addClass(panels, HIDE);

                _clearTimer();

                enterTimer = S.later(function () {
					DOM.addClass(ev.currentTarget, WRAPHOVER);
					DOM.removeClass(panel, HIDE);
				},enterDuration)
            });
            Event.on(triggers, 'mouseleave', function (ev) {
                var panel = DOM.children(ev.currentTarget, '.J_MtNavSub');
				
				_clearEnterTimer();

                timer = S.later(function () {
                    DOM.addClass(panel, HIDE);
                	DOM.removeClass(ev.currentTarget, WRAPHOVER);
                }, duration);
            });
            Event.on(panels, 'mouseenter', _clearTimer);
            Event.on(panels, 'mouseleave', function (ev) {
                var panel = this,
					trigger = DOM.parent(ev.target,'LI');
                timer = S.later(function () {
                    DOM.addClass(panel, HIDE);
                	DOM.removeClass(trigger, WRAPHOVER);
                }, duration);
            });
        },

        // 初始化 Mytaobao 的公告条
        _initNotice: function (){

            // 判断一下是否有全网的公告，以确保不会同时显示两个公告条
            if (!DOM.get('#system-announce')){
                DOM.show('#J_MtNotice');
                DOM.addClass('body', 'mt-notice-on');
            }
        },

        // 初始化用户头像
        _initAvatar: function () {
            var avatarBox = DOM.get('#J_MtAvatarBox'),
                avatar = DOM.get('#J_MtAvatar'),
                avatarOperationTrigger = DOM.get('#J_MtAvatarOperation'),
                defaultAvatarUrl = 'http://a.tbcdn.cn/app/sns/img/default/avatar-120.png';

            if (avatarBox){
                // 注册修改/上传头像的交互事件
                Event.on(avatarBox, 'mouseenter', function (ev) {
                    DOM.removeClass(avatarOperationTrigger, 'hide');
                });
                Event.on(avatarBox, 'mouseleave', function (ev) {
                    DOM.addClass(avatarOperationTrigger, 'hide');
                });
            }
            
        },

        // 初始化侧边栏
        _initMenu: function () {
            var trigger = DOM.query('.J_MtIndicator'),
                    FOLD = 'fold',
                    UNFOLD = 'un' + FOLD;

            Event.on(trigger, 'click', function (ev) {
                ev.preventDefault();

                var trigger = ev.target,
                        container = DOM.parent(trigger, 'DD');

                DOM.hasClass(container, FOLD) ? DOM.replaceClass(container, FOLD, UNFOLD) : DOM.replaceClass(container, UNFOLD, FOLD);
            });
        },

        // 初始化顶部导航中的搜索条
        _initSearch: function () {
            var search = DOM.get('#J_MtSearch'),
                searchForm = DOM.get('FORM', search),
                searchInput = DOM.get('.J_MtSearchQuery', searchForm),
                placeholder = DOM.attr(searchInput, 'placeholder'),
				self = this;

            // 初始化 placeholder
            if (S.UA.ie) {
                searchInput.value = placeholder;

                Event.on(searchInput, 'focus', function () {
                    if (S.trim(searchInput.value) === placeholder) {
                        searchInput.value = '';
                    }

                    DOM.addClass(search, 'mt-focus');
                });

                Event.on(searchInput, 'blur', function () {
                    if (S.trim(searchInput.value) === '') {
                        searchInput.value = placeholder;
                    }

                    DOM.removeClass(search, 'mt-focus');
                });
            }

            // 表单的简单验证
            Event.on(searchForm, 'submit', function (ev) {
                ev.halt();

                var inputValue = S.trim(searchInput.value);

                if (inputValue !== '' && inputValue !== placeholder) {
                    searchForm.submit();
                }
                
                else {
                    S.use('overlay', function () {
                        var html, mtPopup;

                        html = '<div class="mt-overlay"><table class="mt-panel"><tbody>' +
                                   '<tr>' +
                                       '<td class="top_l"></td>' +
                                       '<td class="top_c"></td>' +
                                       '<td class="top_r"></td>' +
                                   '</tr><tr>' +
                                       '<td class="mid_l"></td>' +
                                       '<td class="mid_c">' +
                                           '<div id="mt-content">' +
                                               '<div id="mt-content-panel">' +
                                               '<div class="hd"><h4>小提示</h4></div>' +
                                               '<div class="bd">请输入搜索内容</div>' +
                                               '<div class="ft"><a href="" id="J_MtBtnSure">确定</a></div></div>' +
                                               '<div id="J_MtBtnClose" class="mt-close"></div>' +
                                           '</div>' +
                                       '</td>' +
                                       '<td class="mid_r"></td>' +
                                   '</tr><tr>' +
                                       '<td class="bottom_l"></td>' +
                                       '<td class="bottom_c"></td>' +
                                       '<td class="bottom_r"></td>' +
                                   '</tr>' +
                               '</tbody></table></div>';

                        mtPopup = new S.Overlay({
                            content: html,
                            elCls: 'mt-popup',
                            mask: false,
                            zIndex: 990,
                            align: {
                                points: ['cc', 'cc']
                            }
                        });

                        Event.on(['#J_MtBtnClose', '#J_MtBtnSure'], 'click', function (ev) {
                            ev.preventDefault();
                            mtPopup.destroy();
                        });

                        mtPopup.render();
                    });
                }
            });

            var initSuggestEvent = function (){
                S.use('suggest', function (){
                    var _suggest = new S.Suggest(searchInput, 'http://suggest.taobao.com/sug?code=utf-8', {
                        resultFormat: '',
                        containerCls: 'mt-search-suggest'
                    });
					_suggest.on('beforeStart', function(e) {
						//店铺将不进行联想
						if(self._searchType == 'shop'){	
							return false;
						}
						return true;
					});
					//添加搜索埋点
					S.getScript('http://a.tbcdn.cn/apps/e/tsrp/120301/external/search-stat.js?t=20120309.js', function (){
						set_initiative_id("iz","J_TSearchForm");
					});
                });

                // 当完成一次初始化工作后, 移除事件, 防止重复初始化
                Event.remove(searchInput, 'focus', initSuggestEvent);
            };
            // 搜索提示
            Event.on(searchInput, 'focus', initSuggestEvent);
			/*
			 * 搜索分类（店铺、宝贝）
			 */
			this.initSearchType('.J_Type');
        },

		/*
		 * 搜索分类（店铺、宝贝）
		 * @method initSearchType 
		 * @param {string} node 目标节点
		 * @author add by lingyu
		 */
		initSearchType: function (node) {
			var self = this,
				typeSelect = DOM.query(node,DOM.get('#J_MtSearch'))[0];
			if(!typeSelect) return;
			Event.on(typeSelect,"mouseenter mouseleave",function(ev){
				if (ev.type == 'mouseenter') {
					DOM.addClass(this,"hover");
				} else {
					DOM.removeClass(this,"hover");
				}
			});
			
			Event.on(DOM.query('dt',typeSelect),"click",function(ev){
				var search_a = S.get('a',this),
					search_type = DOM.attr(search_a,"data-type"),  //类别
					search_action = mtConfig._getSearchURL(search_type);
				
				//更换提交地址
				DOM.attr(mtConfig._searchForm(),"action",search_action);
					
				//纪录用户选择
				self._searchType = search_type;
				DOM.prepend(this,S.get('dl',typeSelect));
				DOM.removeClass(typeSelect,"hover");
				
			});			

		},


        // 初始化侧边栏中的 "最近使用过的应用"
        _initRecentApp: function () {
            var href = location.href,
                dailyReg = /daily/,
                isDaily = dailyReg.test(href),
                url = isDaily ? 'http://yingyong.daily.taobao.net/json/menu/recent_apps.htm' : 'http://yingyong.taobao.com/json/menu/recent_apps.htm',
                appListEl = DOM.get('#J_MtRecentApp'),
                firstChild = DOM.children(appListEl)[0];

            if (!appListEl) { return; }

            function _showIcon (){
                // Ready 以后再延时 3 秒去加载 icon
                S.later(function (){
                    var appIcons = DOM.query('.J_AppIcons' ,appListEl);

                    S.each(appIcons, function (el){
                        var backgroundAttr = DOM.attr(el, 'data-background');

                        DOM.css(el, {
                            background: 'url(' + backgroundAttr + ') no-repeat 4px 3px'
                        });
                    })
                }, 0);
            }

            // 这块直接拷贝 2.0/mod/ 下面的 Base 部分来作修改
            var config = {
                success: function () {
                    var recents = window['_i_recents'];

                    if (!S.isPlainObject(recents)) {
                        return config.error();
                    }

                    var appList = recents['apps']['list'],
                        appListMaxSize = recents['apps']['size'];

                    if (!appList.length) {
                        return config.error();
                    }

                    var apps = document.createDocumentFragment(),
                        tpl = '<dd><a target="{target}" href="{url}" data-static="{data}" class="{className}">' +
                              '<s class="J_AppIcons" data-background="{icon}"></s>{name}' + '</a></dd>';

                    // 取数组长度与限制长度的最小值
                    for (var i = Math.min(appListMaxSize, appList.length); i > 0; i--) {
                        var app = appList[i - 1];
                        app = DOM.create(S.substitute(app['icon'] ? tpl : tpl, app));
                        DOM.prepend(app, apps);
                    }

                    DOM.insertAfter(apps, firstChild);
                    apps = null;

                    // 修正因为推荐应用的结构里面用到了position: relative，并且上面的最近
                    // 使用过的应用异步加载，高度动态变化所引起的两个结构叠加的问题
                    DOM.css('#recommend-app', {height: '1%'});

                    _showIcon();
                },

                error: function () {
                    _showIcon();

                    DOM.removeClass(DOM.query('.hide', appListEl), 'hide');
                },

                charset: 'gbk',
                timeout: 10
            };
            S.getScript(url, config);

            // 统计点击
            var map = {
                '我的首页' : 'hp',
                '关注' : 'fw',
                '好友' : 'fd',
                '找人' : 'ffw',
                '找关注' : 'ffd',
                'flash' : 'flash',
                '试用新版' : 'new',
                '返回旧版' : 'old',
                '电影票在线订购' : 'ticket',
                '健康助手' : 'health',
                '育儿中心' : 'baby',
                '美食盒子' : 'food',
                '日志' : 'log',
                '相册' : 'photo',
                '转帖' : 'zt',
                '叽歪' : 'jy',
                '投票' : 'tp',
                '好友印象' : 'hyyx',
                '礼物' : 'lw',
                '淘帮派' : 'tbp',
                '帮我挑' : 'bwt',
                '打听' : 'dt',
                '淘金币' : 'tjb',
                '宝贝分享' : 'bbfx',
                '淘宝达人' : 'tbdr',
                '聚划算' : 'jhs',
                '开心厨房' : 'kxcf',
                '德克萨斯扑克' : 'zzpk',
                '小游戏中心' : 'gc',
                '快乐岛主' : 'kldz',
                '楼一幢' : 'lyz',
                '阳光牧场' : 'ygmc',
                '梦想花园' : 'mxhy',
                '宝贝星球' : 'bbxq',
                '小小战争' : 'xxzz',
                '快乐探宝' : 'kltb',
                '小小夜店' : 'xxyd',
                '联众棋牌' : 'lzqp',
                '果果帮' : 'ggb',
                '我是买家' : 'buyer',
                '我是卖家' : 'seller',
                '账号管理' : 'account',
                '应用中心' : 'app',
                '我的购物车' : 'cart',
                '已买到的宝贝' : 'buyauc',
                '竞拍的宝贝' : 'paauc',
                '我的机票/酒店/保险' : 'airticket',
                '我的彩票' : 'lottery',
                '我的网游' : 'mom',
                '购买过的店铺' : 'buyshop',
                '我的收藏' : 'collect',
                '我的积分' : 'ponit',
                '我的优惠卡券' : 'card',
                '我的信用管理' : 'credit',
                '退款管理' : 'refund',
                '维权管理' : 'weiquan',
                '举报管理' : 'jubao',
                '咨询/回复' : 'zixun',
                '我要付款' : 'alipay',
                '水电煤缴费' : 'sdm',
                '信用卡还款' : 'ccard',
                '编辑头像' : 'bjtx',
                '个人资料' : 'grzl',
                '隐私设置' : 'yssz',
                '个人主页' : 'grzy',
                '信用管理' : 'xygl',
                '我的支付宝' : 'zfb',
                'vip' : 'vip',
                '特权' : 'tq',
                '领取当日淘金币' : 'lqtjb',
                '积分' : 'jf',
                '店铺优惠券' : 'dpyhq',
                '待付款' : 'dfk',
                '待确认收货' : 'dqrsh',
                '待评价' : 'dpj',
                '推荐应用' : 'recommend',
                '淘宝新鲜事' : 'news'
            };

            // Listen to document click event
            Event.on(document, 'click', function(ev) {

                var target = ev.target,
                    targetParent,
                    key = '',
                    para;

                if ('A' !== target.nodeName.toUpperCase()){
                    targetParent = DOM.parent(target, 'A');

                    if (!targetParent){ return; }

                    target = targetParent;
                }

                // data-analytics-key is of higher priority
                if (DOM.attr(target, 'data-analytics-key')) {
                    key = DOM.attr(target, 'data-analytics-key');
                } else {
                    key = S.trim(DOM.html(target));
                }

                // Strips html tags, parentheses, leading/trailing spaces and unwanted numbers
                key = key.replace(/<\/?[a-z][a-z0-9]*[^<>]*>|\(|\)|\d+|^\s+|\s+$/gi, '');
                para = map[key];

                if (para) {
                    var monitor = new Image();
                    monitor.src = 'http://log.mmstat.com/jsclick?mytaobao_sns=' + para + '&cache=' + S.guid();
                    monitor = null;
                }

            });

            //统计代码, by shiran 2011.09.21
            Event.on(document, 'click', function(ev) {

                var target = ev.target,
                    code = DOM.attr(target, 'data-static'),
                    link = (code || 'a' === target.nodeName.toLowerCase()) ? target : DOM.parent(target, 'a'),
                    monitor;

                if (link) {
                    code = DOM.attr(link, 'data-static');
                    monitor = new Image();

                    if (code) {
                        //根据code产生url by xixia.sm 2012-6-15 15:40:16
                        var makeURL = function(code){
                            if(!/^\d+\.\d+\.\d+$/.test(code)){
                                return false;
                            }
                            var info = code.split('.');
                            var url = 'http://log.mmstat.com/tbyy.2.1?'
                                + 'modid=' + info[0]
                                + '&appid=' + info[1]
                                + '&catid=' + info[2]
                                + '&url=' + encodeURI(location.href.replace(location.hash, ''));
                            return url+ new Date().getTime();;
                        }
                        monitor.src = makeURL(code);
                    }

                    if (link.className.toLowerCase() === 'mytaodan') {
                        monitor.src = ' http://log.mmstat.com/sns.2.7.2?tracelog=mytaobaomytaodan&cache=' + new Date().getTime();
                    }

                    monitor = null;
                }
            });



        },

        // 我的淘宝 推荐应用模块
        _initRecommentApp: function (){
            var container = S.get('#J_Recommend_App');
            if(!container){
                return;
            }
            var assetsHost = location.host.indexOf('.daily.') > -1 ? 'assets.daily.taobao.net' : 'a.tbcdn.cn';
            //此时间戳由tms产生
            var timestamp = DOM.attr(container, 'data-timestamp');

            var recomendCss = 'http://' + assetsHost + '/apps/stargate/appcenter/release/API/recommend.css?t=' + timestamp + '.css',
                recomendJs = 'http://' + assetsHost + '/apps/stargate/appcenter/release/API/recommend.js?t=' + timestamp + '.js';
            S.getScript(recomendCss);
            S.getScript(recomendJs, function(){
                try{
                    APPCENTER.recommend.init(container);
                }catch(e){
                    S.log('appcenter: recommend init failed');
                }
            });

        },
		
		//我的圈子左侧弹出层
		myQuanziPop : function() {
			var self = this;
            var timer;
			var MyqzHover = {
				targetNode : DOM.get('#J_quanzi'),
				targetShowNode : DOM.query('dd', DOM.get('#J_quanzi'))[0],
				_mouseEventCallBack : function(ev) {
                    if (ev.type == 'mouseenter') {
                        timer && timer.cancel();
                        MyqzHover._mouseenter();
                    } else if (ev.type == 'mouseleave') {
                        timer = S.later(function () {
                            MyqzHover._mouseleave();
                        }, 300)
                    }
				},
				_clickEventCallBack : function(ev){
					window.open('http://yiqi.taobao.com/plus/group_square.htm?tracelog=sns_yiqi_mytaobao4');
				},
				_mouseEnterLoadData : function(ev) {
					new Image().src = 'http://log.mmstat.com/sns.17.5?cache=' + new Date().getTime();
					self._loadQzData().getQzContData();//导航左侧我的圈子异步加载数据
				},
				_mouseenter : function(){
					DOM.show(this.targetShowNode);
					DOM.addClass(this.targetNode,'myqz-hover');
				},
				_mouseleave : function(){
					DOM.hide(this.targetShowNode);
					DOM.removeClass(this.targetNode,'myqz-hover');
				}
			}
			//执行鼠标hover事件
			if(MyqzHover.targetNode){
				Event.on(MyqzHover.targetNode,'mouseenter mouseleave',MyqzHover._mouseEventCallBack);
				Event.on(MyqzHover.targetShowNode,'mouseenter mouseleave',MyqzHover._mouseEventCallBack);
				//点击跳转至广场页
				Event.on(DOM.query('dt', MyqzHover.targetNode)[0],'click',MyqzHover._clickEventCallBack);
			}
			//鼠标划过加载数据
			Event.on(MyqzHover.targetNode,'mouseenter',MyqzHover._mouseEnterLoadData);

		},
		
		_loadQzData : function() {
			var checkKeys = 'GROUPLUS_567_' + DOM.attr(DOM.get('#J_quanzi'),'data-userid');
			var checkURL = location.host.indexOf('.daily.') > -1 ? 'count.config-vip.taobao.net:8888/counter3' : 'count.tbcdn.cn/counter5';
			var loadQzData = {
				checkResult : false,
				emptyDataNode : DOM.get('#J_EmptyData'),
				targetNode : DOM.get('#J_GetData'),
				_ajaxCheck : 'http://'+ checkURL +'?keys=' + checkKeys,
				_ajaxConURL : DOM.attr(DOM.get('#J_GetData'),'data-url-getcontent'),
				_ajaxCountURL : DOM.attr(DOM.get('#J_GetData'),'data-url-getupdatingcount'),
				_XHR : function(api, data, callback,response) {
					var self = this;
					S.io({
						url:api,
						data:data,
						carset:'gbk',
						dataType:'jsonp',
						jsonp:"callback", 
						success:function(response){
							var result = response;
							callback && callback.call(self,result);
						},
						error:function(msg){
							self._errorExe();
						}
					})
				},
				_errorExe : function(){
						DOM.removeClass(this.emptyDataNode,'hide');
						DOM.addClass(this.targetNode,'hide');
				},
				_requireConCallBack : function (data){
					if(data.code == 1 || data.code == "1"){
						new Image().src = 'http://log.mmstat.com/sns.17.23?cache=' + new Date().getTime();

						DOM.html(this.targetNode,data.html);
						DOM.html(DOM.get('#J_QzNum'),data.groupnum);
					}else{
						this._errorExe();
					}
				},
				_requireCountCallBack : function (data){
				
					if(data.code == 1 || data.code == "1"){
						
						if(data.updateCount == '0') return;
						DOM.html(DOM.get('#J_QzNum'),'(' + data.updateCount + ')');
					}else{
						this._errorExe();
					}
				},
				//异步验证是否请求
				_requireCheck : function (param){
					var self = this;
					S.io({
						url:self._ajaxCheck,
						carset:'gbk',
						dataType:'jsonp',
						jsonp:"callback",
						async:false,
						success:function(response){
							if(response[checkKeys] == 0) {
								self._errorExe();
								return;
							}
							if(param == 'CountData'){
								//异步获取QZ更新数
//								self._XHR(self._ajaxCountURL,null,self._requireCountCallBack);
							}else if(param == 'ContData'){
								//异步获取QZ内容
								self._XHR(self._ajaxConURL,null,self._requireConCallBack);
							}
							self.checkResult = true;
						},
						error:function(msg){
							self._errorExe();
						}
					})
				},
				initCountData : function() {
					this._requireCheck('CountData');
				},
				getQzContData : function() {
					this._requireCheck('ContData');
				}
			}
			return loadQzData;
		}
    };

    MTB.mtBase = mtBase;
    mtBase.init();

})();
