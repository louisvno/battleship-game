function createBattleFieldView (){
    var columnHeaders = ["",1,2,3,4,5,6,7,8,9,10]
    var rowHeaders = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]

    var getTable = document.getElementById("battle-field");
    var setTableHeaders = table.appendChild(
            newColumnHeaderRow(columnHeaders)
            );
    var setTableBody = rowHeaders.forEach( header => {
            table.appendChild(
                newTableRow(header,columnHeaders)
            );
    });
}

function newTableRow(rowHeader,columnHeaders){
    var tr = newTableRowElement();
    var th = newTableHeaderElement();
    th.textContent = rowHeader;
    tr.appendChild(th);

    for(i=1; i <= columnHeaders.length; i++){
      var td = newTableDataElement();
      td.classList.add(rowHeader + columnHeaders[i]);
      tr.appendChild(td);
    }
    return tr;
}

function newColumnHeaderRow(headers){
    var tr = newTableRowElement();
    headers.forEach(function(header, index){
        var th = newTableHeaderElement();
        th.textContent = header;
        tr.appendChild(th);
    })
    return tr;
}

function newTableHeaderElement(){
  return document.createElement("th");
}

function newTableElement(){
  return document.createElement("th");
}

function newTableDataElement(){
  return document.createElement("td");
}

function newTableRowElement(){
  return document.createElement("tr");
}