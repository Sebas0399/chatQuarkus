INSERT INTO companies (name, webhook_token, configs)
SELECT 'Demo Clinic', 'demo-webhook-token', '{}'::jsonb
WHERE NOT EXISTS (
    SELECT 1
    FROM companies
    WHERE name = 'Demo Clinic'
);

INSERT INTO contacts (name, company_id, number, has_notification)
SELECT 'Contacto Demo', c.id, '999999999', false
FROM companies c
WHERE c.name = 'Demo Clinic'
  AND NOT EXISTS (
      SELECT 1
      FROM contacts ct
      WHERE ct.number = '999999999'
  );

INSERT INTO messages (text, is_from_contact, is_from_company, company_id, contact_id)
SELECT 'Mensaje de prueba para validar el flujo', true, false, c.id, ct.id
FROM companies c
JOIN contacts ct ON ct.company_id = c.id
WHERE c.name = 'Demo Clinic'
  AND ct.number = '999999999'
  AND NOT EXISTS (
      SELECT 1
      FROM messages m
      WHERE m.contact_id = ct.id
        AND m.text = 'Mensaje de prueba para validar el flujo'
  );