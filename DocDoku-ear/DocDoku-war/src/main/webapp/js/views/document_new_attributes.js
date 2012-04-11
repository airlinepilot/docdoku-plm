var DocumentNewAttributesView = BaseView.extend({
	collection: function () { return new Backbone.Collection(); },
	template: "document-new-attributes-tpl",
	initialize: function () {
		ListView.prototype.initialize.apply(this, arguments);
		this.events["click .add"] = this.addAttribute;
	},
	rendered: function () {
		this.attributesView = this.addSubView(new DocumentNewAttributeListView({
			el: "#items-" + this.cid,
			collection: this.collection
		}));
	},
	addAttribute: function () {
		this.collection.add({
			name: "",
			type: "TEXT",
			value: ""
		});
	},
});
