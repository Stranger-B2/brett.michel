import psycopg2
import psycopg2.extras

def connect(nom,code):
  conn = psycopg2.connect(
    dbname = 'random',   #nom data base
    #host = 'brett',   #pas important
    user= nom,
    #login =                                #login
    password = code,		#mp database
    cursor_factory = psycopg2.extras.NamedTupleCursor
  )
  conn.autocommit = True
  return conn
