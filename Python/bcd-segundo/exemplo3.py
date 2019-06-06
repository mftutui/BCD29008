from base import Session, engine, Base
from pessoa import Pessoa
from telefone import Telefone

if __name__ == '__main__':
    print('Exemplo 3')

    # Gerando o esquema do BD
    #Base.metadata.create_all(engine)

    session = Session()

    juca = Pessoa('Juca')
    ana = Pessoa('Ana')
    maria = Pessoa('Maria')

    session.add(juca)
    session.add(ana)
    session.add(maria)

    tel_juca = Telefone("3333-3333", juca)
    tel_juca_cel = Telefone("9933-33333", juca)

    session.add(tel_juca)
    session.add(tel_juca_cel)

    session.commit()
    session.close()

    # ORM