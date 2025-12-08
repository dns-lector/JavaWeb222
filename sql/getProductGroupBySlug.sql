SELECT 
	(SELECT COUNT(*) FROM rates r WHERE r.item_id = p.product_id) AS rates_count,
	(SELECT AVG(r.rate_stars) FROM rates r WHERE r.item_id = p.product_id AND r.rate_stars > 0) AS rate_avg,
    pg.*, p.* 
FROM product_groups pg
LEFT JOIN products p ON p.product_group_id = pg.pg_id
WHERE pg.pg_slug = 'glass'
ORDER BY p.product_id
LIMIT 3, 4
-- skip, cnt