World.load("Item Shop", "tiles/item-shop.json");

// A simple jQuery-like wrapper
var $ = function (x) { 
    if (x[0] == '#') {
        return World.asTile(World.$(x));
    } else if (x[0] == '%') {
        return World.asEntity(World.$(x));
    }
    return null;
};

var items = [
    ["health", 100],
    ["attack", 200],
    ["defense", 300],
    ["exp", 400],
    ["transformation", 500],
    ["increase", 600],
    ["rock", 700],
    ["special", 800],
    ["cheat", 900],
    ["magic-stone", 1000]
];

for (var i = 0; i < items.length; i++) {
    (function (n) {
        var item = items[i][0];
        var cost = items[i][1];
        var $item = $('#' + item);
        World.printFixed(item + "\n" + cost, 3, $item.bounds.x, $item.bounds.y);
        $item.touch(function (e) {
            if (!e.is(':controlledPlayer'))
                return;
            World.addMetaArray(item + ":" + cost);
        });
    })(i);
}

