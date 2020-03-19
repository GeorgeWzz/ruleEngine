// import java.util.Map

def test1(Map<String,Object> map) {
    return map.get("a") + map.get("b")
}

Map<String, Object> map1 = new HashMap<>()
map1.put("a", 1)
map1.put("b", 2)

System.out.println(test1(map1))
