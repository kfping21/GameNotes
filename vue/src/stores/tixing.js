import { defineStore } from "pinia";
import {useUserStore} from './user.js'
import DB from '@/utils/db.js'
import date from "@/utils/date.js";
import event from '@/utils/event'
import {getTime} from "@/utils/date";
import {canTongzhiUpdate} from "@/module";


export const useTixingStore = defineStore("tixing", {
	state: () => {
		return {
			lists:[],
			timers:[],
			showDialog: false,
		};
	},
	actions: {
		openDialog(){
			console.log('tixing.openDialog -> setting showDialog true')
			this.showDialog = true;
		},
		closeDialog(){
			this.showDialog = false;
		},
		logout() {
			for(var ci of this.timers){
				clearTimeout(ci);
			}
			this.timers = [];
		},

		login() {
			var userStore = useUserStore();

			var session = userStore.session;
			if(session.cx != '管理员'){
				DB.name("tongzhi")
					.where("yonghu" , session.username)
					.where('issh','否')
					.select()
					.then(res=>{
						// 将查询到的未读通知加入列表，前端展示在左侧面板
						res.forEach(ci=>{
							// push into store lists but do NOT mark as read here
							this.lists.unshift(ci);
						});
					});
			}


		},
	},
});

event.on('login',()=>{
	useTixingStore().login();
});

event.on('logout',()=>{
	useTixingStore().logout();
})
