CREATE VIEW latest_changes_view AS
SELECT c1.*
FROM changes c1 LEFT OUTER JOIN changes c2 ON c1.property_name = c2.property_name AND c1.properties_path = c2.properties_path AND c2.revision_number >  c1.revision_number
WHERE c2.property_name IS NULL;