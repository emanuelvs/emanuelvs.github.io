
var processoList = [];
var backgroundColor = [
    "#FF5733",
    "#33FF57",
    "#5733FF",
    "#33FFFF",
    "#FF33FF",
    "#FFFF33",
    "#3366FF",
    "#FF3366",
    "#66FF33",
    "#6633FF"
]
const ctx = document.getElementById('ganttChart').getContext('2d');
var lastTask = null

const ganttChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: [],
        datasets: [{
            data: [],
            backgroundColor:[
                "#FF5733",
                "#33FF57",
                "#5733FF",
                "#33FFFF",
                "#FF33FF",
                "#FFFF33",
                "#3366FF",
                "#FF3366",
                "#66FF33",
                "#6633FF"
            ]
        }]
    },
    options: {
        indexAxis: 'y',
        scales: {
            x: {
                position: "bottom",
                title: {
                    display: true,
                    text: 'Tempo'
                },
                ticks:{
                    stepSize: 1,
                },
            },
            y: {
                position: 'left',
                title: {
                    display: true,
                    text: 'Processos'
                }
            }
        },
        responsive: false,
        maintainAspectRatio: false,
        plugins: {
            colors: {
                enabled: false,
                forceOverride: true
            },
            legend: {
                display: false
            },
            title: {
                display: true,
                text: 'Execução dos processos'
            },
        },
    },
});

function updateDataGantt(task) {



    ganttChart.data.datasets[0].data.push({x: [task.startTime, task.endTime], y: task.taskName})

    ganttChart.update();
}

function setProcessosLabels() {

    var labels = [];
    for (var i = 0; i < processoList.length; i++) {
        labels.push("P"+i);
    }

    ganttChart.data.labels = labels;
    ganttChart.update();

}

function addProcesso() {

    var tChegada = Number(document.getElementById("tChegada").value);
    var tExecucao = Number(document.getElementById("tExecucao").value);
    var deadline = Number(document.getElementById("deadline").value);
    var prioridade = 0;
    var quantum = Number(document.getElementById("quantum").value);
    var sobrecarga = Number(document.getElementById("sobrecarga").value);
    var qntdPaginas = Number(document.getElementById("nPaginas").value);
    processoList.push({
        tChegada,
        tExecucao,
        deadline,
        prioridade,
        quantum,
        sobrecarga,
        qntdPaginas
    });

    if (tChegada !== "" && tExecucao !== "" && deadline !== "" && quantum !== "" && sobrecarga !== "" && qntdPaginas !== "") {
        let novoProcesso = `<p>T${processoList.length - 1}: T.Chegada: ${tChegada}, T.Execução: ${tExecucao}, Deadline: ${deadline}, Quantum: ${quantum}, Sobrecarga: ${sobrecarga}, N. Páginas: ${qntdPaginas}</p>`;

        $('#processosAdicionados').append(novoProcesso);

    } else {
        alert('Por favor, preencha todos os campos.');
    }


    var totalProcesso = document.getElementById("totalProcessos")
    totalProcesso.innerHTML = processoList.length;
}

function submitInfoSistema() {
    resetSimulation();
    montarDisco();
    setProcessosLabels();

    var request = {
        nProcessos: processoList.length,
        algoritmoEscalonamento: document.querySelector('input[name="tipoEscalonamento"]:checked').value,
        algoritmoPaginacao: document.querySelector('input[name="tipoPaginacao"]:checked').value,
        processos: processoList
    }
    console.log(request)
    buscarEventsSimulador()
    fetch('/api/gantt/escalonar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao iniciar a simulação');
            }
            return response;
        })
        .then(response => response.json())
        .then(res => console.log(res))
}

function buscarEventsSimulador() {

    //const stompClient = Stomp.client("ws://localhost:8080/ws");
    const client = new StompJs.Client();
    client.brokerURL = 'ws://localhost:8080/ws';

    client.onConnect = function () {
        client.subscribe('/escalonador/app', function (event) {
            const eventPayload = JSON.parse(event.body);


            if(eventPayload.action == "GANTT") {
                updateDataGantt(eventPayload.ganttEventDTO);
            }else if(eventPayload.action == "TURNAROUND"){
                var label = "Turnaround: " + eventPayload.turnaroundMedio.toFixed(3);
                var turnaround = document.getElementById("turnaround")
                turnaround.textContent = label;
            }else if(eventPayload.action == "MEMORY") {
                atualizaMemoriaRam(eventPayload.ram);
                atualizaDisco(eventPayload.disco);
            }


        });
    };

    client.onStompError = function (frame) {
        console.error('Erro ao ativar o cliente Stomp:', frame);
    };

    client.activate();

}

function updateVirtualMemoryCell(cellIndex, pageId, processId) {
    const virtualMemoryCells = document.querySelectorAll('#virtualMemoryBoard .memory-cell');
    const cellToUpdate = virtualMemoryCells[cellIndex];
    if(pageId == -1) {
        cellToUpdate.textContent = "Vazio";
        cellToUpdate.style.backgroundColor = "#fff";
    }else {
        cellToUpdate.textContent = `P:${pageId}\r\nT:${processId}`;

    }

    cellToUpdate.style.backgroundColor = backgroundColor[processId]; // Estilo de célula preenchida
}

function updateMemoryCell(cellIndex, pageId, processId) {
    const memoryCells = document.getElementsByClassName('memory-cell');
    const cellToUpdate = memoryCells[cellIndex];
    if(pageId == -1) {
        cellToUpdate.textContent = "Vazio";
        cellToUpdate.style.backgroundColor = "#fff";
    }else{
        cellToUpdate.textContent = `P:${pageId}\r\nT:${processId}`;
    }
    cellToUpdate.style.backgroundColor = backgroundColor[processId];

}

function atualizaMemoriaRam(paginas) {
    for (var i = 0; i < paginas.length; i++) {
        if(paginas[i] != null){
            updateMemoryCell(i, paginas[i].numPagina, paginas[i].idProcesso);
        }else {
            updateMemoryCell(i, -1, -1);
        }

    }
}

function atualizaDisco(paginas) {
    for (var i = 0; i < paginas.length; i++) {
        if(paginas[i] != null) {
            updateVirtualMemoryCell(i, paginas[i].numPagina, paginas[i].idProcesso);
        }else {
            updateVirtualMemoryCell(i, -1, -1);
        }

    }
}

function resetSimulation() {

    ganttChart.data.datasets[0].data = [];
    ganttChart.update();
    for (var i = 0; i < 50; i++) {
        updateMemoryCell(i, -1, -1);
    }
    for (var i = 0; i < 100; i++) {
        updateVirtualMemoryCell(i, -1, -1);
    }
}

function montarDisco() {
    var index = 0;
    for (var i = 0; i < processoList.length; i++) {
        var qntPaginas = processoList[i].qntdPaginas;

        for (var j = 0; j < qntPaginas; j++) {
            updateVirtualMemoryCell(index, j, i);
            index++;
        }
    }
}


