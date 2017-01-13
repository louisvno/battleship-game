function createGameDisplay (destination){
    var columnHeaders = ["",1,2,3,4,5,6,7,8,9,10]
    var rowHeaders = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]

    var table = document.getElementById(destination);
    var createTableHeaders = table.appendChild(
            addColumnHeaderRow(columnHeaders)
            );
    var createTableBody = rowHeaders.forEach( header => {
            table.appendChild(
                addTableRow(header,columnHeaders)
            );
    });
}

function addTableRow(rowHeader,columnHeaders){
    var tr = addTableRowElement();
    var th = addTableHeaderElement();
    th.textContent = rowHeader;
    tr.appendChild(th);

    for(i=1; i < columnHeaders.length; i++){
      var td = addTableDataElement();
      td.setAttribute("data-coordinate",rowHeader + columnHeaders[i]);
      tr.appendChild(td);
    }
    return tr;
}

function addColumnHeaderRow(headers){
    var tr = addTableRowElement();
    headers.forEach(function(header, index){
        var th = addTableHeaderElement();
        th.textContent = header;
        tr.appendChild(th);
    })
    return tr;
}

function addTableHeaderElement(){
  return document.createElement("th");
}

function addTableElement(){
  return document.createElement("th");
}

function addTableDataElement(){
  return document.createElement("td");
}

function addTableRowElement(){
  return document.createElement("tr");
}