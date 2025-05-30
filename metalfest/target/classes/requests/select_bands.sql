SELECT 
    Band.ID AS BandID,
    Band.Name AS BandName,
    Band.Country AS BandCountry,
    Stage.Name AS StageName,
    Performance.Date AS PerformanceDate,
    Performance.TimeStart AS PerformanceStart,
    Performance.TimeEnd AS PerformanceEnd
FROM Band
INNER JOIN Performance ON Band.ID = Performance.Band_id
INNER JOIN Stage ON Performance.Stage_id = Stage.ID;
