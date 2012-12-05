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
    ["health", 100, "HealingPotion"],
    ["attack", 200, "AttackPotion"],
    ["defense", 300, "DefensePotion"],
    ["exp", 400, "EXPPotion"],
    ["transformation", 500, "TransformationPotion"],
    ["increase", 600, "CheatWonBattles"],
    ["rock", 700, "Rock"],
    ["special", 800, "SpecialPotion"],
    ["cheat", 900, "CheatLostBattles"],
    ["magic-stone", 1000, "MagicMoveStone"]
];

$('#exit').touch(function (e) {
    World.exit();
});

for (var i = 0; i < items.length; i++) {
    (function (n) {
        var item = items[i][0];
        var cost = items[i][1];
        var klass = items[i][2];
        var $item = $('#' + item);
        World.printFixed(item + "\n" + cost, 3, $item.bounds.x, $item.bounds.y);
        $item.touch(function (e) {
            if (!e.is(':controlledPlayer'))
                return;
            World.addMetaArray(klass + ":" + cost);
        });
    })(i);
}

