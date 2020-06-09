material_list = ['wooden', 'golden', 'stone', 'iron', 'diamond', 'netherite']
tool_list = ['axe', 'pickaxe', 'shovel', 'hoe', 'sword']
types_list = ['head', 'handle', 'both']

for material in material_list:
    for tool in tool_list:
        for typ in types_list:
            fo = open("minecraft-{0:s}_{1:s}-{2:s}.json".format(material, tool, typ), "w")
            fo.write("{")
            fo.write(" \"parent\": \"item/{0:s}_{1:s}\",".format(material, tool))
            fo.write(" \"textures\": {")
            fo.write(" \"layer0\": \"item/{0:s}_{1:s}\",".format(material, tool))
            if(typ == "head"):
                fo.write(" \"layer1\": \"potiontipped:item/{0:s}\"".format(tool))
            elif(typ == "handle"):
                fo.write(" \"layer1\": \"potiontipped:item/handle\"")
            else:
                fo.write(" \"layer1\": \"potiontipped:item/{0:s}\",".format(tool))
                fo.write(" \"layer2\": \"potiontipped:item/handle\"")
            fo.write("} }")

