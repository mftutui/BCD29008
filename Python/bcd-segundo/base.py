from sqlalchemy import create_engine
from sqlalchemy.ext.automap import _declarative_base
from sqlalchemy.orm import sessionmaker

engine = create_engine("sqlite:///lab05-ex3.sqlite")
Session = sessionmaker(bind=engine)

Base = _declarative_base()