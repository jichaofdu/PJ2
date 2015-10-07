/** 
 * @fileOverview �ҵ��Ա��Ļ���ģ��, ��Ҫ����ҳͷ����, ����, �Լ�������Ĺ��ܵ�
 * ��ʼ��, ������ SNS 1.1, ͬʱӦ����Թ���ģ�������, ��Ϊ���ں�������ҳ���б�
 * ����, ���ӵĻ�����, Ӧ����������������������, ���ܵ�һ
 *
 * @author: ���� <tblonggang@gmail.com>
 */

(function () {
    var S = KISSY,
            DOM = S.DOM,
            Event = S.Event;

    // IE �м��� html5 ��ǩ, ����������ǰ, ��֤
    // ��鲻����Ϊ�����Ľű������ʹ��ҳ�����
    if (S.UA.ie) {
        var tagNames = ['header', 'firgure', 'article', 'section', 'aside', 'footer', 'nav'];
        S.each(tagNames, function (el) {
            document.createElement(el);
        });
    }

    // ���� domain Ϊ������, ��ʵ�ֿ������ͨ��
    try {
        // ��� host Ϊ xxxdaily.daily.taobao.net, ���� daily.taobao.com ֮
        // ���, ���ܻ����𱨴������� try catch ��Χ
        if (DOM.get('#J_ITouchDomain') || DOM.get('#J_quanzi')){
            document.domain = location.host.indexOf('daily') > -1 ?
                'taobao.net' : 'taobao.com';
        }
    } catch (err){}

    // MyTaobao �����ռ�
    S.namespace('MTB', true);

    MTB = {
        _version: '3.0',
        _description: 'MyTaobao Namespace'
    }

	//MyTaobaoҳ������	
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
		//�������ͱ����洢,Ĭ��Ϊ����
		_searchType: 'item',

        // ѡ�����˵�
        selectSideMenuItem: function (id) {
            // ��ʷ����, ѡ�����˵��Ĺ�����Ҫ��JS��ʵ��,
            // ��Ϊ��ģ����Ҫǣ�浽���Ӧ�÷�, ��ʱ�������
            // ��Э�����޸Ĺ���
            var item, foldParentNode;

            item = DOM.get('#' + id);
            if (!item) {
                return;
            }

            // �� item ��λ����Ӧ�� A ��ǩ��
            if (item.nodeName.toUpperCase() !== 'A') {
                item = DOM.get('A', item);
            }

            DOM.addClass(item, 'selected');

            // �ж��Ƿ������۵��˵�����
            foldParentNode = DOM.parent(item, '.J_MtSideTree');
            if (!foldParentNode) {
                return
            }

            DOM.replaceClass(foldParentNode, 'fold', 'unfold');
        },

        init: function () {
            var host = this;

            // ������������ȫ��, ����ʷҳ�����
            window.selectItem = host.selectSideMenuItem;

            // ��ʼ�� Mytaobao �Ĺ�����
            S.available('#J_MtNotice', function (){
                host._initNotice();
            });

            // ��ʼ������ͷ��
            S.available('#J_MtSideMenu', function () {
                host._initAvatar();
            });

            // ����Ҫ�Ķ���, ��ŵ������ȥ���������¼�
            S.ready(function () {
				//��ർ���ҵ�Ȧ�ӵ�����
                if(DOM.get('#J_quanzi')){
				host.myQuanziPop();
//                host._loadQzData().initCountData();//��������ҵ�Ȧ���첽���ظ�����
                }
                // ������ hover Ч��
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

        // ��ʼ��������
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

        // ��ʼ�� Mytaobao �Ĺ�����
        _initNotice: function (){

            // �ж�һ���Ƿ���ȫ���Ĺ��棬��ȷ������ͬʱ��ʾ����������
            if (!DOM.get('#system-announce')){
                DOM.show('#J_MtNotice');
                DOM.addClass('body', 'mt-notice-on');
            }
        },

        // ��ʼ���û�ͷ��
        _initAvatar: function () {
            var avatarBox = DOM.get('#J_MtAvatarBox'),
                avatar = DOM.get('#J_MtAvatar'),
                avatarOperationTrigger = DOM.get('#J_MtAvatarOperation'),
                defaultAvatarUrl = 'http://a.tbcdn.cn/app/sns/img/default/avatar-120.png';

            if (avatarBox){
                // ע���޸�/�ϴ�ͷ��Ľ����¼�
                Event.on(avatarBox, 'mouseenter', function (ev) {
                    DOM.removeClass(avatarOperationTrigger, 'hide');
                });
                Event.on(avatarBox, 'mouseleave', function (ev) {
                    DOM.addClass(avatarOperationTrigger, 'hide');
                });
            }
            
        },

        // ��ʼ�������
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

        // ��ʼ�����������е�������
        _initSearch: function () {
            var search = DOM.get('#J_MtSearch'),
                searchForm = DOM.get('FORM', search),
                searchInput = DOM.get('.J_MtSearchQuery', searchForm),
                placeholder = DOM.attr(searchInput, 'placeholder'),
				self = this;

            // ��ʼ�� placeholder
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

            // ���ļ���֤
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
                                               '<div class="hd"><h4>С��ʾ</h4></div>' +
                                               '<div class="bd">��������������</div>' +
                                               '<div class="ft"><a href="" id="J_MtBtnSure">ȷ��</a></div></div>' +
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
						//���̽�����������
						if(self._searchType == 'shop'){	
							return false;
						}
						return true;
					});
					//����������
					S.getScript('http://a.tbcdn.cn/apps/e/tsrp/120301/external/search-stat.js?t=20120309.js', function (){
						set_initiative_id("iz","J_TSearchForm");
					});
                });

                // �����һ�γ�ʼ��������, �Ƴ��¼�, ��ֹ�ظ���ʼ��
                Event.remove(searchInput, 'focus', initSuggestEvent);
            };
            // ������ʾ
            Event.on(searchInput, 'focus', initSuggestEvent);
			/*
			 * �������ࣨ���̡�������
			 */
			this.initSearchType('.J_Type');
        },

		/*
		 * �������ࣨ���̡�������
		 * @method initSearchType 
		 * @param {string} node Ŀ��ڵ�
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
					search_type = DOM.attr(search_a,"data-type"),  //���
					search_action = mtConfig._getSearchURL(search_type);
				
				//�����ύ��ַ
				DOM.attr(mtConfig._searchForm(),"action",search_action);
					
				//��¼�û�ѡ��
				self._searchType = search_type;
				DOM.prepend(this,S.get('dl',typeSelect));
				DOM.removeClass(typeSelect,"hover");
				
			});			

		},


        // ��ʼ��������е� "���ʹ�ù���Ӧ��"
        _initRecentApp: function () {
            var href = location.href,
                dailyReg = /daily/,
                isDaily = dailyReg.test(href),
                url = isDaily ? 'http://yingyong.daily.taobao.net/json/menu/recent_apps.htm' : 'http://yingyong.taobao.com/json/menu/recent_apps.htm',
                appListEl = DOM.get('#J_MtRecentApp'),
                firstChild = DOM.children(appListEl)[0];

            if (!appListEl) { return; }

            function _showIcon (){
                // Ready �Ժ�����ʱ 3 ��ȥ���� icon
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

            // ���ֱ�ӿ��� 2.0/mod/ ����� Base ���������޸�
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

                    // ȡ���鳤�������Ƴ��ȵ���Сֵ
                    for (var i = Math.min(appListMaxSize, appList.length); i > 0; i--) {
                        var app = appList[i - 1];
                        app = DOM.create(S.substitute(app['icon'] ? tpl : tpl, app));
                        DOM.prepend(app, apps);
                    }

                    DOM.insertAfter(apps, firstChild);
                    apps = null;

                    // ������Ϊ�Ƽ�Ӧ�õĽṹ�����õ���position: relative��������������
                    // ʹ�ù���Ӧ���첽���أ��߶ȶ�̬�仯������������ṹ���ӵ�����
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

            // ͳ�Ƶ��
            var map = {
                '�ҵ���ҳ' : 'hp',
                '��ע' : 'fw',
                '����' : 'fd',
                '����' : 'ffw',
                '�ҹ�ע' : 'ffd',
                'flash' : 'flash',
                '�����°�' : 'new',
                '���ؾɰ�' : 'old',
                '��ӰƱ���߶���' : 'ticket',
                '��������' : 'health',
                '��������' : 'baby',
                '��ʳ����' : 'food',
                '��־' : 'log',
                '���' : 'photo',
                'ת��' : 'zt',
                'ߴ��' : 'jy',
                'ͶƱ' : 'tp',
                '����ӡ��' : 'hyyx',
                '����' : 'lw',
                '�԰���' : 'tbp',
                '������' : 'bwt',
                '����' : 'dt',
                '�Խ��' : 'tjb',
                '��������' : 'bbfx',
                '�Ա�����' : 'tbdr',
                '�ۻ���' : 'jhs',
                '���ĳ���' : 'kxcf',
                '�¿���˹�˿�' : 'zzpk',
                'С��Ϸ����' : 'gc',
                '���ֵ���' : 'kldz',
                '¥һ��' : 'lyz',
                '��������' : 'ygmc',
                '���뻨԰' : 'mxhy',
                '��������' : 'bbxq',
                'ССս��' : 'xxzz',
                '����̽��' : 'kltb',
                'ССҹ��' : 'xxyd',
                '��������' : 'lzqp',
                '������' : 'ggb',
                '�������' : 'buyer',
                '��������' : 'seller',
                '�˺Ź���' : 'account',
                'Ӧ������' : 'app',
                '�ҵĹ��ﳵ' : 'cart',
                '���򵽵ı���' : 'buyauc',
                '���ĵı���' : 'paauc',
                '�ҵĻ�Ʊ/�Ƶ�/����' : 'airticket',
                '�ҵĲ�Ʊ' : 'lottery',
                '�ҵ�����' : 'mom',
                '������ĵ���' : 'buyshop',
                '�ҵ��ղ�' : 'collect',
                '�ҵĻ���' : 'ponit',
                '�ҵ��Żݿ�ȯ' : 'card',
                '�ҵ����ù���' : 'credit',
                '�˿����' : 'refund',
                'άȨ����' : 'weiquan',
                '�ٱ�����' : 'jubao',
                '��ѯ/�ظ�' : 'zixun',
                '��Ҫ����' : 'alipay',
                'ˮ��ú�ɷ�' : 'sdm',
                '���ÿ�����' : 'ccard',
                '�༭ͷ��' : 'bjtx',
                '��������' : 'grzl',
                '��˽����' : 'yssz',
                '������ҳ' : 'grzy',
                '���ù���' : 'xygl',
                '�ҵ�֧����' : 'zfb',
                'vip' : 'vip',
                '��Ȩ' : 'tq',
                '��ȡ�����Խ��' : 'lqtjb',
                '����' : 'jf',
                '�����Ż�ȯ' : 'dpyhq',
                '������' : 'dfk',
                '��ȷ���ջ�' : 'dqrsh',
                '������' : 'dpj',
                '�Ƽ�Ӧ��' : 'recommend',
                '�Ա�������' : 'news'
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

            //ͳ�ƴ���, by shiran 2011.09.21
            Event.on(document, 'click', function(ev) {

                var target = ev.target,
                    code = DOM.attr(target, 'data-static'),
                    link = (code || 'a' === target.nodeName.toLowerCase()) ? target : DOM.parent(target, 'a'),
                    monitor;

                if (link) {
                    code = DOM.attr(link, 'data-static');
                    monitor = new Image();

                    if (code) {
                        //����code����url by xixia.sm 2012-6-15 15:40:16
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

        // �ҵ��Ա� �Ƽ�Ӧ��ģ��
        _initRecommentApp: function (){
            var container = S.get('#J_Recommend_App');
            if(!container){
                return;
            }
            var assetsHost = location.host.indexOf('.daily.') > -1 ? 'assets.daily.taobao.net' : 'a.tbcdn.cn';
            //��ʱ�����tms����
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
		
		//�ҵ�Ȧ����൯����
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
					self._loadQzData().getQzContData();//��������ҵ�Ȧ���첽��������
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
			//ִ�����hover�¼�
			if(MyqzHover.targetNode){
				Event.on(MyqzHover.targetNode,'mouseenter mouseleave',MyqzHover._mouseEventCallBack);
				Event.on(MyqzHover.targetShowNode,'mouseenter mouseleave',MyqzHover._mouseEventCallBack);
				//�����ת���㳡ҳ
				Event.on(DOM.query('dt', MyqzHover.targetNode)[0],'click',MyqzHover._clickEventCallBack);
			}
			//��껮����������
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
				//�첽��֤�Ƿ�����
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
								//�첽��ȡQZ������
//								self._XHR(self._ajaxCountURL,null,self._requireCountCallBack);
							}else if(param == 'ContData'){
								//�첽��ȡQZ����
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
