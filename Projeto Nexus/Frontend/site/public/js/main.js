var itensSelecionados = document.getElementsByClassName('item-type');
var divTextMoni = document.getElementById('text-moni');
var guilherme = document.getElementById("guilherme");
var jeremias = document.getElementById("jeremias");
var lisandra = document.getElementById("lisandra");
var lucas = document.getElementById("lucas");
var marcos = document.getElementById("marcos");
var michael = document.getElementById("michael");
var status = document.getElementById("status");
var spanG = document.getElementById('text-back-card-G')
var spanJ = document.getElementById('text-back-card-J')
var spanLi = document.getElementById('text-back-card-Li')
var spanLu = document.getElementById('text-back-card-Lu')
var spanMa = document.getElementById('text-back-card-Ma')
var spanMi = document.getElementById('text-back-card-Mi')
var frontG = document.getElementById('front-card-G')
var frontJ = document.getElementById('front-card-J')
var frontLi = document.getElementById('front-card-Li')
var frontLu = document.getElementById('front-card-Lu')
var frontMa = document.getElementById('front-card-Ma')
var frontMi = document.getElementById('front-card-Mi')


for (var i = 0; i < itensSelecionados.length; i++) {
    itensSelecionados[i].addEventListener('click', function click(e) {
        if (this.classList.contains('cpu')) {

            divTextMoni.style.opacity = 0;
            setTimeout(function(){
            divTextMoni.innerHTML = '';
            divTextMoni.innerHTML = `
                <div class="left-side-moni">
                    <h2 class="title-moni">
                        O monitoramento do uso do seu processador.
                    </h2>
                    <p class="subtext-moni">
                        Nós do Nexus iremos sempre estar monitorando o seu processador
                        para que voce possa entender quaisprocessos e tarefas estão
                        comprometendo o seu sistema.
                    </p>
                </div>
                <div class="right-side-moni">
                    <img src="./assets/img-nexus/cpu.svg" alt="Desenho de um processador" srcset="">
                </div>`
                divTextMoni.style.opacity = 1;
            },200)
            
        } else if (this.classList.contains('ram')) {

            divTextMoni.style.opacity = 0;

            setTimeout(function () {
            
            divTextMoni.innerHTML = '';
            divTextMoni.innerHTML = `
            <div class="left-side-moni">
                <h2 class="title-moni">
                    O monitoramento do uso das suas memorias.
                </h2>
                <p class="subtext-moni">
                    Nós do Nexus iremos sempre estar monitorando a sua memoria
                    para que voce possa entender quais processos e tarefas estão
                    comprometendo o seu sistema.
                </p>
            </div>
            <div class="right-side-moni">
                <img src="./assets/img-nexus/memorias 1.svg" alt="Desenho de um processador" srcset="">
            </div>`
            
            divTextMoni.style.opacity = 1;

            }, 200)

        } else if (this.classList.contains('disco')) {

            divTextMoni.style.opacity = 0;
            

            setTimeout(function(){

                divTextMoni.innerHTML = '';
                divTextMoni.innerHTML = `
                <div class="left-side-moni">
                    <h2 class="title-moni">
                        O monitoramento do uso do disco.
                    </h2>
                    <p class="subtext-moni">
                        Nós do Nexus iremos sempre estar monitorando o uso do seu disco
                        para que voce possa entender quais arquivos e tarefas estão
                        comprometendo o seu sistema.
                    </p>
                </div>
                <div class="right-side-moni">
                    <img src="./assets/img-nexus/disco.svg" alt="Desenho de um processador" srcset="">
                </div>`

                divTextMoni.style.opacity = 1;

            },200)
            
        } else {
            console.log('nao achei diabo nenhum')
        }
        for (var j = 0; j < itensSelecionados.length; j++) {
            itensSelecionados[j].classList.remove('item-selected');
        }
        this.classList.add('item-selected');
    });
}

guilherme.addEventListener("click", function(){
    guilherme.classList.toggle("flip");
    if(spanG.classList.contains('none') )  {
        setTimeout(function(){


            frontG.classList.add('none');
            frontG.classList.remove('flex');
            spanG.classList.remove('none');
            spanG.classList.add('flex');
        },500)
    }
    else  {
        setTimeout(function(){
        frontG.classList.add('flex');
        frontG.classList.remove('none')
        spanG.classList.remove('flex');
        spanG.classList.add('none');
    }, 500)
    }
    
})
jeremias.addEventListener("click", function(){
    jeremias.classList.toggle("flip");
    if(spanJ.classList.contains('none') )  {
        setTimeout(function(){


            frontJ.classList.add('none');
            frontJ.classList.remove('flex');
            spanJ.classList.remove('none');
            spanJ.classList.add('flex');
        },500)
    }
    else  {
        setTimeout(function(){
        frontJ.classList.add('flex');
        frontJ.classList.remove('none')
        spanJ.classList.remove('flex');
        spanJ.classList.add('none');
    }, 500)
    }
})
lisandra.addEventListener("click", function(){
    lisandra.classList.toggle("flip");
    if(spanLi.classList.contains('none') )  {
        setTimeout(function(){


            frontLi.classList.add('none');
            frontLi.classList.remove('flex');
            spanLi.classList.remove('none');
            spanLi.classList.add('flex');
        },500)
    }
    else  {
        setTimeout(function(){
        frontLi.classList.add('flex');
        frontLi.classList.remove('none')
        spanLi.classList.remove('flex');
        spanLi.classList.add('none');
    }, 500)
    }
})
lucas.addEventListener("click", function(){
    lucas.classList.toggle("flip");
    if(spanLu.classList.contains('none') )  {
        setTimeout(function(){


            frontLu.classList.add('none');
            frontLu.classList.remove('flex');
            spanLu.classList.remove('none');
            spanLu.classList.add('flex');
        },500)
    }
    else  {
        setTimeout(function(){
        frontLu.classList.add('flex');
        frontLu.classList.remove('none')
        spanLu.classList.remove('flex');
        spanLu.classList.add('none');
    }, 500)
    }
})
marcos.addEventListener("click", function(){
    marcos.classList.toggle("flip");
    if(spanMa.classList.contains('none') )  {
        setTimeout(function(){


            frontMa.classList.add('none');
            frontMa.classList.remove('flex');
            spanMa.classList.remove('none');
            spanMa.classList.add('flex');
        },500)
    }
    else  {
        setTimeout(function(){
        frontMa.classList.add('flex');
        frontMa.classList.remove('none')
        spanMa.classList.remove('flex');
        spanMa.classList.add('none');
    }, 500)
    }
})
michael.addEventListener("click", function(){
    michael.classList.toggle("flip");
    if(spanMi.classList.contains('none') )  {
        setTimeout(function(){


            frontMi.classList.add('none');
            frontMi.classList.remove('flex');
            spanMi.classList.remove('none');
            spanMi.classList.add('flex');
        },500)
    }
    else  {
        setTimeout(function(){
        frontMi.classList.add('flex');
        frontMi.classList.remove('none')
        spanMi.classList.remove('flex');
        spanMi.classList.add('none');
    }, 500)
    }
})

var menuSwitch = document.getElementById("menuSwitch");
var nav = document.getElementById("navMobile");
var sectionsArray = document.getElementsByTagName("section");

menuSwitch.addEventListener("click", function(){
    if (nav.style.display === "flex") {
        nav.style.display = "none"
        menuSwitch.style.color = "#9E6AE1"
        nav.classList.remove("requires-no-scroll")

        for(i = 0; i < sectionsArray.length; i++){
            sectionsArray[i].style.filter = "brightness(1)"
        }

    } else {
        nav.style.display = "flex"

        for(i = 0; i < sectionsArray.length; i++){
            sectionsArray[i].style.filter = "brightness(0.9)"
        }
        menuSwitch.style.color = "white"
        nav.classList.add("requires-no-scroll")
    }
});




