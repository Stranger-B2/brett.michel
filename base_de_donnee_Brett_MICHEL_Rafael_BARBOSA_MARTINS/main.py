import random
import db
import datetime as dt
import psycopg2.extras
import psycopg2
from flask import Flask, render_template, request, redirect, url_for, session, jsonify
import secrets
app = Flask(__name__)
app.secret_key = f'{secrets.token_hex()}'.encode()

def compare(date1, date_2,x):
    date2=str(date_2)
    temp=date1[0]+date1[1]+date1[2]+date1[3]
    temp2=date2[0]+date2[1]+date2[2]+date2[3]
    if (int(temp)<int(temp2)):
        return -1
    if (int(temp)>int(temp2)):
        return 1
    temp=date1[5]+date1[6]
    temp2=date2[5]+date2[6]
    if (int(temp)<int(temp2)):
        return -1
    if (int(temp)>int(temp2)):
        return 1
    temp=date1[8]+date1[9]
    temp2=date2[8]+date2[9]
    if (int(temp)<int(temp2)):
        return -1
    if (int(temp)>int(temp2)):
        return 1
    temp=date1[11]+date1[12]
    temp2=date2[11]+date2[12]
    if (int(temp)<(int(temp2)+x)):
        return -1
    if (int(temp)>(int(temp2)+x)):
        return 1
    temp=date1[14]+date1[15]
    temp2=date2[14]+date2[15]
    if (int(temp)<int(temp2)):
        return -1
    if (int(temp)>int(temp2)):
        return 1
    return 0

@app.route("/Signaler")
def Signaler():
    return render_template("Signaler.html")

@app.route("/SignalerFait", methods = ["POST"])
def SignalerFait():
    im=request.form.get("im",None)
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur.execute("(SELECT id_vehicule,imatriculation FROM vehicule ORDER BY id_vehicule) EXCEPT (SELECT id_vehicule,imatriculation FROM vehicule WHERE imatriculation IS NULL)")
    for result in cur:
        if (result[1]==im): 
            cur.execute("UPDATE vehicule SET fonctionnel = False WHERE id_vehicule = %s",(result[0],))
            cur.close()
            return render_template("SignalerFait.html", reussite=True)
    cur.close()
    return render_template("SignalerFait.html",reussite=False)

@app.route("/Reservation")
def Reservation():
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur.execute("SELECT DISTINCT type FROM vehicule")
    l = []
    for result in cur:
        l.append(result[0])
    cur.execute("SELECT code_station , count(ville) FROM station Group by code_station ORDER BY code_station")  
    i = {}
    for result in cur:
        i[result[0]]= result[1]
    cur.execute("SELECT dateR,id_vehicule,type FROM reservation_v NATURAL JOIN vehicule WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    n = {}
    for result in cur:
        n[result[0]]= [result[1],result[2]]
    cur.execute("SELECT dateR,id_borne,type FROM reservation_b NATURAL JOIN borne WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    a = {}
    for result in cur:
        a[result[0]]= [result[1],result[2]]
    cur.close()
    return render_template("Reservation.html",vehicule=l, station=i, resv=n, resb=a, mes="")

@app.route("/ReservationFaite", methods = ["POST"])
def Reservationfaite():
    bv=request.form.get("genre",None)
    sta=request.form.get("station",None)
    vhe=request.form.get("vehicule",None)
    dat=request.form.get("datetime",None)
    v=True
    mes=""
    if (vhe=='voiture'):
        x=3
    else: x=1
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur2 = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    if (bv=="v"):
        cur.execute(" SELECT id_vehicule,vehicule.type,code_station FROM vehicule JOIN borne ON stationne=id_borne WHERE fonctionnel=True")
        idV=0
        for result in cur:
            if (result[1]==vhe and result[2]==int(sta)):
                cur2.execute(" SELECT dateR FROM reservation_v WHERE id_vehicule=%s",(result[0],))
                for res in cur2:                                                                                           
                    if (compare(dat,res[0],0)>=0 and compare(dat, res[0],x)<=0):
                        v=False
                if v:
                    idV=result[0]
    else:
        cur.execute(" SELECT id_borne,type,code_station FROM borne")
        idV=0
        for result in cur:
            if (result[1]==vhe and result[2]==int(sta)):
                cur2.execute(" SELECT dateR FROM reservation_b WHERE id_borne=%s",(result[0],))
                for res in cur2:                                                                                           
                    if (compare(dat,res[0],0)>=0 and compare(dat, res[0],x)<=0):
                        v=False
                if v:
                    idV=result[0]
    if (idV!=0 and compare(dat,dt.datetime.now(),0)>0 ):
        if (bv=="v"):
            cur.execute("INSERT INTO reservation_v (id_abonne,dateR,id_vehicule,code_station) VALUES (%s,%s,%s,%s)",(session["id"],dat,idV,sta))
            mes="Vous avez réservé un "+vhe+" pour le "+dat
        else:
            cur.execute("INSERT INTO reservation_b  (id_abonne,dateR,id_borne) VALUES (%s,%s,%s)",(session["id"],dat,idV))
            mes="Vous avez réservé un "+vhe+" pour le "+dat
    elif (idV==0):
        if (bv=="v"):
            mes="il n'y a plus de véhicule disponible dans cette station"
        else:
            mes="il n'y a plus de borne disponible dans cette station"
    elif (compare(dat,dt.datetime.now(),0)<=0):
        mes="Vous ne pouvez pas faire une telle reservation à cette date là"
    cur.execute("SELECT DISTINCT type FROM vehicule")
    i = []
    for result in cur:
        i.append(result[0])
    cur.execute("SELECT code_station , count(ville) FROM station Group by code_station ORDER BY code_station")
    j = {}
    for result in cur:
        j[result[0]]= result[1]
    cur.execute("SELECT dateR,id_vehicule,type FROM reservation_v NATURAL JOIN vehicule WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    n = {}
    for result in cur:
        n[result[0]]= [result[1],result[2]]
    cur.execute("SELECT dateR,id_borne,type FROM reservation_b NATURAL JOIN borne WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    a = {}
    for result in cur:
        a[result[0]]= [result[1],result[2]]
    cur.close()
    return render_template("Reservation.html",vehicule=i,station=j,  resv=n, resb=a,mes=mes)

@app.route("/annulation",methods = ["POST"])
def annulation():
    b=request.form.get("date",None)
    g=request.form.get("genre",None)
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    if (g=="v"):
        cur.execute('DELETE FROM reservation_v WHERE id_abonne=%s and dateR=%s',(session["id"],b))
    else:
        cur.execute('DELETE FROM reservation_b WHERE id_abonne=%s and dateR=%s',(session["id"],b))
    cur.execute("SELECT DISTINCT type FROM vehicule")
    l = []
    for result in cur:
        l.append(result[0])
    cur.execute("SELECT code_station , count(ville) FROM station Group by code_station ORDER BY code_station")  
    i = {}
    for result in cur:
        i[result[0]]= result[1]
    cur.execute("SELECT dateR,id_vehicule,type FROM reservation_v NATURAL JOIN vehicule WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    n = {}
    for result in cur:
        n[result[0]]= [result[1],result[2]]
    cur.execute("SELECT dateR,id_borne,type FROM reservation_b NATURAL JOIN borne WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    a = {}
    for result in cur:
        a[result[0]]= [result[1],result[2]]
    cur.close()
    return render_template("Reservation.html",vehicule=l, station=i, resv=n, resb=a, mes="")
	
@app.route("/Historique")
def Historique():
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur.execute("SELECT dateR,id_vehicule,code_station FROM reservation_v WHERE (id_abonne=%s) and dateR>%s",(session["id"],dt.datetime.now()))
    l = {}
    for result in cur:
        l[result[0]]= [result[1],result[2]]
    cur.execute("SELECT dateR,id_vehicule,code_station FROM reservation_v WHERE (id_abonne=%s) and dateR<%s",(session["id"],dt.datetime.now()))
    l2 = {}
    for result in cur:
        l2[result[0]]= [result[1],result[2]]
    cur.close()
    return render_template("Historique.html",liste=l,liste2=l2)

@app.route('/statistique', methods=['POST'])
def statistique():
    month_year = request.form.get('month_year')
    if month_year:
        year, month = month_year.split('-')
        conn = db.connect('brett','fun')
        cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
        query = """
        SELECT COUNT(*)
        FROM reservations_v
        WHERE EXTRACT(YEAR FROM dateR) = %s
        AND EXTRACT(MONTH FROM dateR) = %s;
        """

        cur.execute(query, (year, month))
        result = cur.fetchone()

        cur.close()
        return jsonify({'reservations': result[0]})
    return render_template("accueil.html")

@app.route("/Stations_Two2Four")
def Stations_Two2Four():
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur.execute("SELECT station.code_station, count(id_borne) AS b, count(id_vehicule) AS v, ville FROM (station LEFT JOIN borne ON station.code_station=borne.code_station) LEFT JOIN vehicule ON id_borne = stationne Group by station.code_station ORDER BY station.code_station") 
    l = {}
    for result in cur:
        l[result[0]]= [result[1],result[2],result[3]]
    cur.close()
    return render_template("Stations_Two2Four.html",liste=l)


@app.route("/Stations")
def Stations():
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur.execute("SELECT station.code_station, count(id_borne) AS b, count(id_vehicule) AS v, ville FROM (station LEFT JOIN borne ON station.code_station=borne.code_station) LEFT JOIN vehicule ON id_borne = stationne Group by station.code_station ORDER BY station.code_station") 
    l = {}
    for result in cur:
        l[result[0]]= [result[1],result[2],result[3]]
    cur.close()
    return render_template("Stations.html",liste=l)

@app.route("/connexion")
def connexion():
	return render_template("connexion.html", )                                
	
@app.route("/connexion_réussi", methods = ["POST"])
def connexion_réussi():
    a=int(request.form.get("id",None))
    b=request.form.get("mdp",None)
    conn=db.connect('brett','fun')
    cur = conn.cursor(cursor_factory = psycopg2.extras.NamedTupleCursor)
    cur.execute("SELECT id_abonne,mdp FROM abonne")
    l = {}
    for result in cur:
        l[result[0]]= result[1]
    cur.close()
    if (a in l.keys()):
        if (l[a]==b):                                                                                        
            session["id"]=a
            return render_template("accueil.html")
    return render_template("connexion.html", )

@app.route("/")
def acc():
    return render_template("deconnexion.html")

@app.route("/deconnexion")
def deconnexion():
    session["id"]=""
    del session["id"]
    return render_template("deconnexion.html")

@app.route("/accueil")
def accueil():
    return render_template("accueil.html")
	
if __name__ == '__main__':
    app.run()
