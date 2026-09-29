-- Migracao da restricao antiga. Ela incluia o status e, por isso, impedia
-- cancelar uma consulta remarcada quando ja existia um cancelamento anterior.
ALTER TABLE consultas ADD COLUMN IF NOT EXISTS chave_reserva VARCHAR(10);

UPDATE consultas
SET chave_reserva = 'ATIVA'
WHERE status <> 'CANCELADA'
  AND chave_reserva IS NULL;

ALTER TABLE consultas
    DROP CONSTRAINT IF EXISTS uk_consulta_medico_data_horario_status;
