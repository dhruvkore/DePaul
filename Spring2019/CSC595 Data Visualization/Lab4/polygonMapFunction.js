var PolyGonOverMap = function(){

  var newPGOM = {
    createPolygonOverMap: function(svg, data){

      d3.json("https://d3js.org/us-10m.v1.json", function(error, us) {
        if (error) throw error;

        var nation = topojson.feature(us, us.objects.nation);

        svg.append("path")
            .attr("fill", "red")
            .attr("d", d3.geoPath()(nation));

        svg.append("path")
            .attr("fill", "black")
            .attr("stroke", "white")
            .attr("d", d3.geoPath(d3.geoIdentity().clipExtent([[100, 100], [860, 500]]))(nation));

        //Just draws Blue Squares form CSV on top of the map
        svg.selectAll("empty")
        .data(data)
          .enter()
          .append("rect")
            .attr("fill", "blue")
            .attr("x", function(d){
              return d.x;
            })
            .attr("y", function(d){
              return d.y;
            })
            .attr("width", function(d){
              return d.width;
            })
            .attr("height", function(d){
              return d.height;
            })
            .on("click", function(d){
              newPGOM.dispatch.call("clicked", {}); 
            });
      });

    },

    dispatch: d3.dispatch("clicked")
  }

  return newPGOM;
}
