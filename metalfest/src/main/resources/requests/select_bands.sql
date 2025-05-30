SELECT 
    Band.ID AS BandID,
    Band.Name AS BandName,
    Band.Country AS BandCountry,
    Stage.Name AS StageName,
    Performance.Date AS PerformanceDate,
    Performance.TimeStart AS PerformanceStart,
    Performance.TimeEnd AS PerformanceEnd
FROM Band
LEFT JOIN Performance ON Band.ID = Performance.Band_id
LEFT JOIN Stage ON Performance.Stage_id = Stage.ID;
