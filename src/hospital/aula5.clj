(ns hospital.aula5
  (:use clojure.pprint))

(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)]
    (cond (= :urgente situacao) :sempre-autorizado
          (contains? paciente :plano) :plano-de-saude
          :else :credito-minimo)))

(defmulti deve-assinar-pre-autorizacao? tipo-de-autorizador)

(defmethod deve-assinar-pre-autorizacao? :sempre-autorizado [pedido]
  false)

(defmethod deve-assinar-pre-autorizacao? :plano-de-saude [pedido]
           (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(defmethod deve-assinar-pre-autorizacao? :credito-minimo [pedido]
  (>= (:valor pedido 0) 50))

(println "É urgente, deve assinar?")
(let [particular {:id 15, :nome "Guilherme", :nascimento "18-09-1981", :situacao :urgente}
      plano {:id 16, :nome "Lucia", :nascimento "24/10/1991", :situacao :urgente, :plano [:raio-x, :ultrassom]}]
  (pprint (deve-assinar-pre-autorizacao? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))

(println "É normal, deve assinar?")
(let [particular {:id 15, :nome "Guilherme", :nascimento "18-09-1981", :situacao :normal}
      plano {:id 16, :nome "Lucia", :nascimento "24/10/1991", :situacao :normal, :plano [:raio-x, :ultrassom]}]
  (pprint (deve-assinar-pre-autorizacao? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))