
public class ArvoreAVL {

	private NoAVL raiz;

	public ArvoreAVL(NoAVL raiz) {
		this.raiz = raiz;
	}

	public NoAVL root() {
		return raiz;
	}

	public boolean isEmpty() {
		return (this.raiz == null);
	}

	public void inserir(Object elemento, int key) {
		if (this.raiz == null) {
			this.raiz = new NoAVL(null, elemento, key);
		}
		NoAVL noInserido = inserirRecusivo(this.raiz, elemento, key);
		System.out.println("No inserido: " + noInserido.getElemento());

		NoAVL no_balancear = noInserido.getPai().atualizarFbInserir(noInserido.lado());

		if (no_balancear != null) {
			rebalancear(no_balancear);
		}
	}

	public NoAVL inserirRecusivo(NoAVL raiz, Object elemento, int key) {
		if (key < raiz.getKey()) {
			if (raiz.getEsq() == null) {
				raiz.setEsq(new NoAVL(raiz, elemento, key));
				return raiz.getEsq();
			}
			return inserirRecusivo(raiz.getEsq(), elemento, key);
		} else {
			if (raiz.getDir() == null) {
				raiz.setDir(new NoAVL(raiz, elemento, key));
				return raiz.getDir();
			}
			return inserirRecusivo(raiz.getDir(), elemento, key);
		}
	}

	public void rebalancear(NoAVL no) {
		if (no.getFb() < 2 && no.getFb() > -2) {
			System.out.println("Não fez rotação!");
		}
		if (no.getFb() == 2) {
			if (no.getEsq().getFb() >= 0) {
				RSD(no);
			} else {
				RDD(no);
			}
		} else if (no.getFb() == -2) {
			if (no.getDir().getFb() <= 0) {
				System.out.println("Fez rotação RSE!");
				RSE(no);
			} else {
				RDE(no);
			}
		}
	}

	public NoAVL Busca(int valor) {
		return BuscaRecursiva(this.raiz, valor);
	}

	private NoAVL BuscaRecursiva(NoAVL raiz, int valor) {
		if (raiz == null) {// se a arvore estiver vazia
			return null;// retorna falso
		}
		if (valor == raiz.getKey()) {// caso encontremos o valor na arvore
			return raiz;
		}
		return valor < raiz.getKey() ? BuscaRecursiva(raiz.getEsq(), valor) : BuscaRecursiva(raiz.getDir(), valor);// se
																													// //
																													// direito
	}

	public int alturaNo(NoAVL no) {
		if (no.getEsq() == null && no.getDir() == null) {// se o no não tiver filho a altura é zero
			return 0;
		} else {
			if (alturaNo(no.getEsq()) > alturaNo(no.getDir())) {
				return (1 + alturaNo(no.getEsq()));
			} else {
				return (1 + alturaNo(no.getDir()));
			}
		}
	}

	public Object remover(int key) {
		NoAVL no_removido = Busca(key);
		if (no_removido == null) {
			return null;
		}
		if (no_removido.getEsq() == null && no_removido.getDir() == null) {// se é um no folha
			if (no_removido == root()) {// se o no é a raiz
				this.raiz = null;
			} else {// se o no não for a raiz
				if (no_removido.lado() == 1) {// se o no a ser removido estiver ao lado esquerdo do pai
					no_removido.getPai().setEsq(null);
				} else {// vai esta do lado direito
					no_removido.getPai().setDir(null);
				}
				NoAVL no_balancear = no_removido.getPai().atualizarFbRemover(no_removido.lado());
				if (no_balancear != null) {
					rebalancear(no_balancear);
				}
			}
		}else if((no_removido.getDir() == null && no_removido.getEsq() != null) || (no_removido.getDir() != null && no_removido.getEsq() == null)) {//Caso o no so tenha um filho
			if(no_removido == root()) {//se o nó for a raiz
				if (no_removido.getEsq() != null) {//caso a raiz tenha o filho esquerdo
                    this.raiz = no_removido.getEsq();//a raiz agora vai ser o filho esquedo do no a ser removido
                    no_removido.getEsq().setPai(null);//atualiza o pai do nó
                } else {//caso tenha somente o filho direito
                    this.raiz = no_removido.getDir();
                    no_removido.getDir().setPai(null);
                }
                raiz.setFb(0);
			}
		}else if(no_removido.getDir() != null && no_removido.getEsq() != null) {//Caso o no tenha os dois filhos
			NoAVL sucessor = sucessor(no_removido);//No que vai assumir o lugar do no removido
			no_removido.setElemento(sucessor.getElemento());//trocamos o elemento das posiçoes
			no_removido.setKey(sucessor.getKey());//trocamos a key
			if(sucessor.getEsq() != null) {//se o sucessor tem filho a esquerda
				if(sucessor.lado() == -1) {//se o sucessor for do lado direito
					sucessor.getPai().setDir(sucessor.getEsq());//o filho direito do pai do sucessor vai ser o filho esquerdo do sucessor
				}else {//se o sucessor for do lado esquedo
					sucessor.getPai().setEsq(sucessor.getEsq());//o filho esquerdo do pai do sucessor vai ser o filho esquerdo do sucessor
				}
				sucessor.getEsq().setPai(sucessor.getPai());//o pai do do filho esquerdo do sucessor vai ser o pai do sucessor
				NoAVL no_balancear = sucessor.atualizarFbRemover(1);//atualizamos o fb da arvore percorrendo pela posição do sucessor
				if(no_balancear != null) {
					rebalancear(no_balancear);//rebalanceia a arvore caso necessario
				}
			}else {//se o sucessor nao tem filho a esquerda
				if(sucessor.lado() == 1) {//se o lado do sucessor for a esquerda do seu pai
					sucessor.getPai().setDir(null);//o filho direito do pai do sucessor é null
				}else {//se for do lado direito
					sucessor.getPai().setEsq(null);//o filho esquerdo do pai do sucessor é null
				}
				NoAVL no_balancear = sucessor.getPai().atualizarFbRemover(sucessor.lado());//atualiza os fb apartir do pai do sucessor
				if(no_balancear != null) {
					rebalancear(no_balancear);//rebalanceia a arvore caso necessario
				}
			}
		}
		return no_removido.getElemento();
	}

	public NoAVL sucessor(NoAVL no) {
		if (no.getEsq() != null) {
			NoAVL noEsquerda = no.getEsq();
			while (noEsquerda.getDir() != null) {
				noEsquerda = noEsquerda.getDir();
			}
			return noEsquerda;
		} else {
			return no.getDir();
		}
	}
	
	public int depth(NoAVL v) {
        int depth = profundidade(v);
        return depth;
    	}

    	public int profundidade(NoAVL n) {
        if (n == this.root)
            return 0;
        else
            return 1 + profundidade(n.getPai());
	}
	
	public int height() {
        	return altura(root);
	}
	
	public int altura(NoAVL n) {
		if (n.getEsq() == null && n.getDir() == null){
		    return 0;
		}
		int h = 0;
		Iterator<NoAVL> i = n.children();
		while (i.hasNext()) {
		    h = Math.max(h, altura(i.next()));
		}
		return 1 + h;
    	}
	public void RSD(NoAVL no) {
		NoAVL aux = no.getEsq();// no auxiliar vai ser igual ao filho esquerdo no no que nao esta balanceado
		no.setEsq(aux.getDir());// o filho esquerdo do no que nao ta balanceado vai ser o filho direito do no
								// auxiliar
		if (aux.getDir() != null) {// caso a sub-arvore esquerda do no tenha filho direito
			aux.getDir().setPai(no);// o pai desse filho sera o no desbalanceado
		}
		aux.setDir(no);// o filho direito do no auxiliar vai ser o no que estava desbalanceado
		aux.setPai(no.getPai());// o pai da sub-arvore esquerda do no desbalceado vai ser o pai do no
								// desbalanceado
		if (no.getPai() != null) {// se o no desbalanceado não for a raiz da arvore
			if (no.lado() == -1) {// e o no desbaceado seja um filho direito
				no.getPai().setDir(aux);// o filho direito do pai do no desbalabceado vai ser a sub-arvore esquerda do
										// no desbalanceado
			} else {
				no.getPai().setEsq(aux);// se nao for a sub-arvore vai ficar do lado esquerdo do no pai do no
										// desbalanceado
			}
		} else {// se nao for nenhum dos casos a sub-arvore esquerda do no debalanceado sera a
				// raiz
			raiz = aux;
		}
		no.setPai(aux);// e o pai do no desbalanceado vai ser o antigo filho esquerdo dele
		no.setFb(no.getFb() - 1 - Math.max(aux.getFb(), 0));
		aux.setFb(aux.getFb() - 1 + Math.min(no.getFb(), 0));
	}

	public void RSE(NoAVL no) {
		NoAVL aux = no.getDir();
		no.setDir(aux.getEsq());
		if (aux.getEsq() != null) {
			aux.getEsq().setPai(no);
		}
		aux.setEsq(no);
		aux.setPai(no.getPai());
		if (no.getPai() != null) {
			if (no.lado() == -1)
				no.getPai().setDir(aux);
			else
				no.getPai().setEsq(aux);
		} else {
			raiz = aux;
		}
		no.setPai(aux);
		no.setFb(no.getFb() + 1 - Math.min(aux.getFb(), 0));
		aux.setFb(aux.getFb() + 1 + Math.max(no.getFb(), 0));
	}

	public void RDD(NoAVL no) {
		RSE(no.getEsq());
		RSD(no);
	}

	public void RDE(NoAVL no) {
		RSD(no.getDir());
		RSE(no);
	}
	public void display() {
        System.out.println("Arvore---------\n");
        if (isEmpty()) {
            System.out.println("vazia");
        }
        int altura = (height() + 1) * 2 - 1;
        int largura = (int) Math.pow(2, height() + 2);
        String[][] table = new String[altura][largura];
        int i, j;
        for (i = 0; i < altura; i++) {
            for (j = 0; j < largura; j++) {
                table[i][j] = " ";
            }
        }
		
        /*Iterator<NoAVL> nos = nos();
        int ordem = 0;
        System.out.print("em ordem : ");
        while (nos.hasNext()) {
            NoAVL n = nos.next();
            System.out.print(n.getKey()+" ");
            int profundidade = profundidade(n) * 2;
            table[profundidade][ordem++] += n.getKey();
        }*/
		
        System.out.println();
        for (i = 0; i < altura; i++) {
            for (j = 0; j < largura; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("fb de root = " + this.root.getFb());
    }
}
}