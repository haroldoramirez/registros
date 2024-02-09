let fileListTableBody = document.getElementById("fileListTableBody");
let fileList = [];
let currentDate = getCurrentDate();
let uploadFilesStatus = false;
let invalidFile = false;
let repeatedFile = false;
let porcentagem = 0;

$("#documentTypeUpload").change(function() {
	let tipoDocumento = $("#documentTypeUpload option:selected").val();
	if (tipoDocumento === "") {
		hideFormElements();
		hideModalMessageInfo();
		disableActionsButtons();
	} else {
		$("#divUploadFileInput").show();
	}
});

$("#fileInput").change(function() {
	initFileList();
	hideModalMessageInfo();
	for (let file of fileInput.files) {
		let fileTitle = file.name.substring(0, file.name.indexOf("."));
		let fileLength = fileTitle.length;
		if (fileLength < 5 && !isNaN(parseInt(fileTitle) && !["application/x-zip-compressed", "application/x-msdownload", ""].includes(file.type))) {
			fileList.push(file);
		} else {
			invalidFile = true;
		}
	}
	if(invalidFile) {
		showModalMessageInfo();
	}
	renderFileList();
});

function enviarDados() {
	hideButtonLimpar();
	changeStatusSendButtonSalvar();
	hideErrorMessage();
	showSendMessage();
	percentageLoader();
	setTimeout(() => {
		requestLiferay(
	        $portletAjaxURLSalvarArquivo,
	        "POST",
	        "formUploadFile"
	    );
	    disableFormInputs();
	}, 800);
}

function requestLiferay(url, method, formId) {
	AUI().use("aui-io-request", function (a) {
	    a.io.request(url, {
	        method: method,
	        form: {
	            id: formId,
	            upload: true
	        },
	        sync: true,
	        on: {
			    complete: function(data) {
					if (!data.details[1].responseText) {
						throw new Error("Estrutura de dados inválida enviada pelo servidor.");
					}
					let retorno = JSON.parse(data.details[1].responseText);
					if (retorno.sucesso) {
						hideSendMessage();
						showSendMessageSuccess();
						porcentagem = 98;
						setTimeout(() => {
							resetForm();
							initFileList();
						}, 4000);
					} else {
						console.log("Ocorreu um erro ao realizar o upload de arquivos: ", retorno);
						hideSendMessage();
						hideSendMessageSuccess();
						showErrorMessage();
						revertStatusButtonSalvar();
						enableButtonSalvar();
						showButtonLimpar();
						porcentagem = 0;
						enableFormInputs();
						enableButtonRemover();
					}
				}
	        }
	    });
	});
}

$("#limpar").click(function() {
	resetForm();
	initFileList();
});

function removeFile(index) {
	fileList.splice(index, 1);
	renderFileList();
}

function initFileList() {
	fileList = [];
	uploadFilesStatus = false;
	invalidFile = false;
}

function resetForm() {
	document.getElementById("formUploadFile").reset();
	disableActionsButtons();
	hideFormElements();
	hideModalMessageInfo();
	hideSendMessage();
	hideSendMessageSuccess();
	hideErrorMessage();
	hideRepeatedFileMessage();
	revertStatusButtonSalvar();
	showButtonLimpar();
	enableFormInputs();
	porcentagem = 0;
}

function hideFormElements() {
	$("#divUploadFileInput").hide();
	$("#fileListDiv").hide();
}

function disableActionsButtons() {
	disabledButtonSalvar();
	disabledButtonLimpar();
}

function enableFormInputs() {
	let inputTipoDocumentosUpload = $("#documentTypeUpload");
	let inputFile = $("#fileInput");
	inputTipoDocumentosUpload.removeAttr("disabled");
	inputFile.removeAttr("disabled");
}

function disableFormInputs() {
	//campos com o atributo disabled nao sao enviado para o submit
    let inputTipoDocumentosUpload = $("#documentTypeUpload");
    let inputFile = $("#fileInput");
    inputTipoDocumentosUpload.attr("disabled", "disabled");
    inputFile.attr("disabled", "disabled");
    for (let i = 0; i < fileList.length; i++) {
		$("#fileListTableBody").children().eq(i).find(":button").prop("disabled", true);
    }
}

function renderFileList() {
	let listDataTransfer = new DataTransfer();
	if (fileList.length > 0) {
		$("#fileListDiv").show();
		fileListTableBody.innerHTML = "";
		fileList.forEach(function(file, index) {
			listDataTransfer.items.add(file);
			let indexItem = index;
			let fileDisplayEl = document.createElement("tr");
			fileDisplayEl.innerHTML =
				"<td>" + currentDate + "</td>" +
				"<td>" + file.name + "</td>" +
				"<td>" + file.size + "</td>" +
				"<td>" +
					"<button type='button' name='actionRemove' onclick='removeFile(" + indexItem + ")'>" +
						"<svg width='16' height='16' viewBox='0 0 16 16' fill='none' xmlns='http://www.w3.org/2000/svg'> <path d='M0.800049 3.2001C0.800049 2.3176 1.51755 1.6001 2.40005 1.6001H6.40005V4.8001C6.40005 5.2426 6.75755 5.6001 7.20005 5.6001H10.4V6.5651C8.55255 7.0876 7.20005 8.7851 7.20005 10.8001C7.20005 12.2776 7.92755 13.5826 9.04255 14.3826C8.96255 14.3951 8.88255 14.4001 8.80005 14.4001H2.40005C1.51755 14.4001 0.800049 13.6826 0.800049 12.8001V3.2001ZM10.4 4.8001H7.20005V1.6001L10.4 4.8001ZM11.6 14.4001C9.61255 14.4001 8.00005 12.7876 8.00005 10.8001C8.00005 8.8126 9.61255 7.2001 11.6 7.2001C13.5875 7.2001 15.2 8.8126 15.2 10.8001C15.2 12.7876 13.5875 14.4001 11.6 14.4001ZM13.0825 9.8826C13.2375 9.7276 13.2375 9.4726 13.0825 9.3176C12.9275 9.1626 12.6725 9.1626 12.5175 9.3176L11.6 10.2351L10.6825 9.3176C10.5275 9.1626 10.2725 9.1626 10.1175 9.3176C9.96255 9.4726 9.96255 9.7276 10.1175 9.8826L11.035 10.8001L10.1175 11.7176C9.96255 11.8726 9.96255 12.1276 10.1175 12.2826C10.2725 12.4376 10.5275 12.4376 10.6825 12.2826L11.6 11.3651L12.5175 12.2826C12.6725 12.4376 12.9275 12.4376 13.0825 12.2826C13.2375 12.1276 13.2375 11.8726 13.0825 11.7176L12.165 10.8001L13.0825 9.8826Z' fill='#EB5757'/> </svg>" +
						"<span>Remover</span>" +
					"</button>" +
				"</td>";
			fileListTableBody.appendChild(fileDisplayEl);
		});
		fileInput.files = listDataTransfer.files;
		verifyDuplicates();
		enableButtonLimpar();
	} else {
		$("#fileListDiv").hide();
		disableActionsButtons();
	}
}

function getCurrentDate() {
	let date = new Date();
	let currentDate = date.toLocaleDateString("pt-BR", {
		year: "numeric",
		month: "2-digit",
		day: "2-digit",
	})
	return currentDate;
}

function openUploadModal() {
	$("#uploadFileModal").modal({
	  	backdrop: "static",
  		keyboard: false
	});
}

function closeUploadModal() {
	if (fileList.length > 0 && uploadFilesStatus === false) {
		let resultado = confirm("Existem documento(s) pendente(s) não salvo(s). Deseja realmente sair?");
		if (resultado) {
			$("#uploadFileModal").modal("hide");
			resetForm();
			initFileList();
		}
	} else {
		$("#uploadFileModal").modal("hide");
		resetForm();
		initFileList();
	}
}

function hideModalMessageInfo() {
	$("#mensagem-info-modal").hide();
}

function showModalMessageInfo() {
	$("#mensagem-info-modal").show();
}

function hideButtonLimpar() {
	$("#limpar").hide();
}

function hideButtonSalvar() {
	$("#salvar").hide();
}

function showButtonLimpar() {
	$("#limpar").show();
}

function changeStatusSendButtonSalvar() {
	$("#salvar").html("<svg width='20' height='20' viewBox='0 0 20 20' fill='none' xmlns='http://www.w3.org/2000/svg'><path d='M12 4C12 4.82844 11.3284 5.5 10.5 5.5C9.67156 5.5 9 4.82844 9 4C9 3.17156 9.67156 2.5 10.5 2.5C11.3284 2.5 12 3.17156 12 4ZM10.5 15.5C9.67156 15.5 9 16.1716 9 17C9 17.8284 9.67156 18.5 10.5 18.5C11.3284 18.5 12 17.8284 12 17C12 16.1716 11.3284 15.5 10.5 15.5ZM17 9C16.1716 9 15.5 9.67156 15.5 10.5C15.5 11.3284 16.1716 12 17 12C17.8284 12 18.5 11.3284 18.5 10.5C18.5 9.67156 17.8284 9 17 9ZM5.5 10.5C5.5 9.67156 4.82844 9 4 9C3.17156 9 2.5 9.67156 2.5 10.5C2.5 11.3284 3.17156 12 4 12C4.82844 12 5.5 11.3284 5.5 10.5ZM5.90381 13.5962C5.07537 13.5962 4.40381 14.2677 4.40381 15.0962C4.40381 15.9246 5.07537 16.5962 5.90381 16.5962C6.73225 16.5962 7.40381 15.9246 7.40381 15.0962C7.40381 14.2678 6.73222 13.5962 5.90381 13.5962ZM15.0962 13.5962C14.2677 13.5962 13.5962 14.2677 13.5962 15.0962C13.5962 15.9246 14.2677 16.5962 15.0962 16.5962C15.9246 16.5962 16.5962 15.9246 16.5962 15.0962C16.5962 14.2678 15.9246 13.5962 15.0962 13.5962ZM5.90381 4.40381C5.07537 4.40381 4.40381 5.07537 4.40381 5.90381C4.40381 6.73225 5.07537 7.40381 5.90381 7.40381C6.73225 7.40381 7.40381 6.73225 7.40381 5.90381C7.40381 5.07537 6.73222 4.40381 5.90381 4.40381Z' fill='white'/></svg> <span>Enviando...</span>");
	let btnSalvar = $("#salvar");
	btnSalvar.attr("disabled", "disabled");
}

function showSendMessageSuccess() {
	$("#mensagemSucesso").show();
}

function hideSendMessageSuccess() {
	$("#mensagemSucesso").hide();
}

function revertStatusButtonSalvar() {
	$("#salvar").html("Salvar");
}

function showSendMessage() {
	$("#mensagemEnvio").show();
}

function hideSendMessage() {
	$("#mensagemEnvio").hide();
}

function showErrorMessage() {
	$("#mensagemErro").show();
}

function hideErrorMessage() {
	$("#mensagemErro").hide();
}

function enableButtonSalvar() {
	let btnSalvar = $("#salvar");
	btnSalvar.removeAttr("disabled");
}

function disabledButtonSalvar() {
	let btnSalvar = $("#salvar");
	btnSalvar.attr("disabled", "disabled");
}

function enableButtonLimpar() {
	let btnLimpar = $("#limpar");
	btnLimpar.removeAttr("disabled");
}

function disabledButtonLimpar() {
	let btnLimpar = $("#limpar");
	btnLimpar.attr("disabled", "disabled");
}

function enableButtonRemover() {
    for (let i = 0; i < fileList.length; i++) {
		$("#fileListTableBody").children().eq(i).find(":button").prop("disabled", false);
    }
}

function showRepeatedFileMessage() {
	$("#mensagem-info-arquivo-repetido-modal").show();
}

function hideRepeatedFileMessage() {
	$("#mensagem-info-arquivo-repetido-modal").hide();
}

function verifyDuplicates() {
	let repeatedFileList = [];
	let files = fileList.map(x => x.name.substring(0, x.name.indexOf(".")));
	let set = new Set(files);
	repeatedFileList = files.filter(item => {
		if (set.has(item)) {
			set.delete(item);
		} else {
			return item;
		}
	});
	if (repeatedFileList.length > 0) {
	  	$("#mensagemInfoArquivosRepetidos").text("O(s) arquivo(s) " + repeatedFileList + " está(ão) com o mesmo nome e para o correto envio, a plataforma só permite arquivos com nomes únicos.");
		showRepeatedFileMessage();
		disabledButtonSalvar();
	} else {
		hideRepeatedFileMessage();
		enableButtonSalvar();
	}
}

function percentageLoader() {
    let linhaPorcentagem = $("#porcentagem");
	$("#porcentagem").show();
    let t = setInterval(function() {
        porcentagem = porcentagem + 1;
        linhaPorcentagem.text(porcentagem + "% enviado, ");
        if (porcentagem === 99) {
            clearInterval(t);
            porcentagem = 0;
        }
    }, 195);
}