UPDATE tweet
SET crdate = CURRENT_TIMESTAMP() - INTERVAL (uid*60) second;