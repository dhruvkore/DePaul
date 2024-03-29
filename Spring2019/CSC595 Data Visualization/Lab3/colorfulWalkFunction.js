var colorfulWalk = function(canvas, data){

  var context = canvas.getContext("2d"),
      width = canvas.width,
      height = canvas.height,
      color = d3.scaleSequential(d3.interpolateRainbow).domain([0, 1000]),
      randomX = d3.randomNormal(0.3),
      randomY = d3.randomNormal(0);

  render();

  function render() {
    var mainWalk = blackWalk();

    context.clearRect(0, 0, width, height);
    context.lineJoin = "round";
    context.lineCap = "round";
    context.lineWidth = 1.5;
    context.strokeStyle = "black";
    renderWalk(mainWalk);

    context.globalCompositeOperation = "multiply";
    context.lineWidth = 1;
    for (var i = 0; i < mainWalk.length; i += 2) {
      var branchStart = mainWalk[i],
          x0 = branchStart[0],
          y0 = branchStart[1];
      for (var j = 0; j < 1; ++j) {
        context.strokeStyle = color(i + (Math.random() - 0.5) * 50);
        var x1 = x0, y1 = y0;
        for (var k = 0, m = 20; k < m; ++k) {
          context.globalAlpha = (m - k - 1) / m;
          var pieceWalk = randomWalk(x1, y1, 10),
              pieceEnd = pieceWalk[pieceWalk.length - 1];
          renderWalk(pieceWalk);
          x1 = pieceEnd[0];
          y1 = pieceEnd[1];
        }
        context.globalAlpha = 1;
      }
    }
  }

  function renderWalk(walk) {
    var i, n = walk.length;
    context.beginPath();
    context.moveTo(walk[0][0], walk[0][1]);
    for (i = 1; i < n; ++i) {
      context.lineTo(walk[i][0], walk[i][1]);
    }
    context.stroke();
  }

  function blackWalk() {
    var points = new Array(data.length);
    points[0] = [0, 0];
    
    var arrayLength = data.length;
    for (var i = 0; i < arrayLength; i++) {
        points[i] = [
            data[i].x,
            data[i].y
          ];
    }

    return points;
  }

  function randomWalk(x0, y0, n) {
    var points = new Array(n), i;
    points[0] = [x0, y0];
    for (i = 1; i < n; ++i) {
      points[i] = [
        x0 += randomX() * 2,
        y0 += randomY() * 2
      ];
    }
    return points;
  }

};
