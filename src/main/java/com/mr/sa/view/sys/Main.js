var rootNodesLoaded = false;

// @Bind view.onReady
!function(self, tipSkin) {
	view.get("^skin-item").objects.each(function(menuItem) {
		if (menuItem.get("userData") == $setting["widget.skin"]) {
			menuItem.set("checked", true);
			return false;
		}
	});

}


// @Bind ^skin-item.onClick
!function(self, actionChangeSkin) {
	actionChangeSkin.set("parameter", self.get("userData")).execute(function() {
		window.location.reload();
	});
}

//@Bind #menuMoreSkin.onClick
!function(self, actionChangeSkin) {
	open("http://dorado7.bsdn.org/skin-builder", "_blank");
}