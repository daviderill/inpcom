-- ----------------------------
-- View structure for "v_inp_buildup"
-- ----------------------------
CREATE VIEW "v_inp_buildup" AS 
SELECT inp_buildup_land_x_pol.landus_id, inp_buildup_land_x_pol.poll_id, inp_buildup_land_x_pol.funcb_type, inp_buildup_land_x_pol.c1, inp_buildup_land_x_pol.c2, inp_buildup_land_x_pol.c3, inp_buildup_land_x_pol.perunit FROM inp_buildup_land_x_pol;

-- ----------------------------
-- View structure for "v_inp_conduit_cu"
-- ----------------------------
CREATE VIEW "v_inp_conduit_cu" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, (st_length2d(arc.the_geom))::numeric(16,3) AS length, arc.z1, arc.z2, cat_mat.n, inp_conduit.q0, inp_conduit.qmax, cat_arc.shape, cat_arc.geom1, cat_arc.curve_id, cat_arc.geom3, cat_arc.geom4, inp_conduit.barrels, inp_conduit.culvert, catch_selection.catch_id FROM ((((arc JOIN cat_arc ON (((arc.arccat_id)::text = (cat_arc.id)::text))) JOIN inp_conduit ON (((arc.arc_id)::text = (inp_conduit.arc_id)::text))) JOIN cat_mat ON (((arc.matcat_id)::text = (cat_mat.id)::text))) JOIN catch_selection ON (((arc.catch_id)::text = (catch_selection.catch_id)::text))) WHERE ((cat_arc.shape)::text = 'CUSTOM'::text);

-- ----------------------------
-- View structure for "v_inp_conduit_no"
-- ----------------------------
CREATE VIEW "v_inp_conduit_no" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, (st_length2d(arc.the_geom))::numeric(16,3) AS length, arc.z1, arc.z2, mat_cat.n, conduit.q0, conduit.qmax, arc_cat.shape, arc_cat.geom1, arc_cat.geom2, arc_cat.geom3, arc_cat.geom4, conduit.barrels, conduit.culvert, catch_selection.catch_id FROM (((((arc JOIN cat_arc arc_cat ON ((((arc.arccat_id)::text = (arc_cat.id)::text) AND ((arc.arccat_id)::text = (arc_cat.id)::text)))) JOIN inp_conduit conduit ON ((((arc.arc_id)::text = (conduit.arc_id)::text) AND ((arc.arc_id)::text = (conduit.arc_id)::text)))) JOIN cat_mat mat_cat ON ((((arc.matcat_id)::text = (mat_cat.id)::text) AND ((arc.matcat_id)::text = (mat_cat.id)::text)))) JOIN catchment ON ((catchment.catch_id = arc.catch_id))) JOIN catch_selection ON ((catch_selection.catch_id = catchment.catch_id))) WHERE ((((arc_cat.shape)::text < 'CUSTOM'::text) OR (((arc_cat.shape)::text > 'CUSTOM'::text) AND ((arc_cat.shape)::text < 'IRREGULAR'::text))) OR ((arc_cat.shape)::text > 'IRREGULAR'::text));

-- ----------------------------
-- View structure for "v_inp_conduit_xs"
-- ----------------------------
CREATE VIEW "v_inp_conduit_xs" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, (st_length2d(arc.the_geom))::numeric(16,3) AS length, arc.z1, arc.z2, mat_cat.n, conduit.q0, conduit.qmax, cat_arc.shape, cat_arc.tsect_id, cat_arc.geom2, cat_arc.geom3, cat_arc.geom4, conduit.barrels, conduit.culvert, catch_selection.catch_id FROM (((((arc JOIN cat_arc ON ((((arc.arccat_id)::text = (cat_arc.id)::text) AND ((arc.arccat_id)::text = (cat_arc.id)::text)))) JOIN inp_conduit conduit ON ((((arc.arc_id)::text = (conduit.arc_id)::text) AND ((arc.arc_id)::text = (conduit.arc_id)::text)))) JOIN cat_mat mat_cat ON ((((arc.matcat_id)::text = (mat_cat.id)::text) AND ((arc.matcat_id)::text = (mat_cat.id)::text)))) JOIN catchment ON ((catchment.catch_id = arc.catch_id))) JOIN catch_selection ON ((catch_selection.catch_id = catchment.catch_id))) WHERE ((cat_arc.shape)::text = 'IRREGULAR'::text);

-- ----------------------------
-- View structure for "v_inp_controls"
-- ----------------------------
CREATE VIEW "v_inp_controls" AS 
SELECT inp_controls.id, inp_controls.text FROM inp_controls ORDER BY inp_controls.id;

-- ----------------------------
-- View structure for "v_inp_coverages"
-- ----------------------------
CREATE VIEW "v_inp_coverages" AS 
SELECT subcatchment.subc_id, inp_coverage_land_x_subc.landus_id, inp_coverage_land_x_subc.percent, catch_selection.catch_id FROM ((inp_coverage_land_x_subc JOIN subcatchment ON ((inp_coverage_land_x_subc.subc_id = subcatchment.subc_id))) JOIN catch_selection ON ((subcatchment.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_curve"
-- ----------------------------
CREATE VIEW "v_inp_curve" AS 
SELECT inp_curve.curve_id, inp_curve.curve_type, inp_curve.x_value, inp_curve.y_value FROM inp_curve ORDER BY inp_curve.id;

-- ----------------------------
-- View structure for "v_inp_divider_cu"
-- ----------------------------
CREATE VIEW "v_inp_divider_cu" AS 
SELECT node.node_id, node.elev, inp_divider.arc_id, inp_divider.divider_type AS type_dicu, inp_divider.qmin, node.ymax, node.y0, node.ysur, node.apond, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN inp_divider ON ((node.node_id = inp_divider.node_id))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) WHERE ((inp_divider.divider_type)::text = 'CUTOFF'::text);

-- ----------------------------
-- View structure for "v_inp_divider_ov"
-- ----------------------------
CREATE VIEW "v_inp_divider_ov" AS 
SELECT node.node_id, inp_divider.arc_id, node.elev, inp_divider.divider_type AS type_diov, node.ymax, node.y0, node.ysur, node.apond, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN inp_divider ON ((node.node_id = inp_divider.node_id))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) WHERE ((inp_divider.divider_type)::text = 'OVERFLOW'::text);

-- ----------------------------
-- View structure for "v_inp_divider_tb"
-- ----------------------------
CREATE VIEW "v_inp_divider_tb" AS 
SELECT node.node_id, node.elev, inp_divider.arc_id, inp_divider.divider_type AS type_ditb, inp_divider.curve_id, node.ymax, node.y0, node.ysur, node.apond, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN inp_divider ON ((node.node_id = inp_divider.node_id))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) WHERE ((inp_divider.divider_type)::text = 'TABULAR'::text);

-- ----------------------------
-- View structure for "v_inp_divider_wr"
-- ----------------------------
CREATE VIEW "v_inp_divider_wr" AS 
SELECT node.node_id, node.elev, inp_divider.arc_id, inp_divider.divider_type AS type_diwr, inp_divider.qmin, inp_divider.ht, inp_divider.cd, node.ymax, node.y0, node.ysur, node.apond, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN inp_divider ON ((node.node_id = inp_divider.node_id))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) WHERE ((inp_divider.divider_type)::text = 'WEIR'::text);

-- ----------------------------
-- View structure for "v_inp_dwf_flow"
-- ----------------------------
CREATE VIEW "v_inp_dwf_flow" AS 
SELECT node.node_id, 'FLOW'::text AS type_dwf, inp_dwf.value, inp_dwf.pat1, inp_dwf.pat2, inp_dwf.pat3, inp_dwf.pat4, catch_selection.catch_id FROM ((catch_selection JOIN node ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_dwf ON ((inp_dwf.node_id = node.node_id)));

-- ----------------------------
-- View structure for "v_inp_dwf_load"
-- ----------------------------
CREATE VIEW "v_inp_dwf_load" AS 
SELECT inp_dwf_pol_x_node.poll_id, node.node_id, inp_dwf_pol_x_node.value, inp_dwf_pol_x_node.pat1, inp_dwf_pol_x_node.pat2, inp_dwf_pol_x_node.pat3, inp_dwf_pol_x_node.pat4, catch_selection.catch_id FROM ((catch_selection JOIN node ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_dwf_pol_x_node ON ((inp_dwf_pol_x_node.node_id = node.node_id)));

-- ----------------------------
-- View structure for "v_inp_evap_co"
-- ----------------------------
CREATE VIEW "v_inp_evap_co" AS 
SELECT inp_evaporation.evap_type AS type_evco, inp_evaporation.evap FROM inp_evaporation WHERE ((inp_evaporation.evap_type)::text = 'CONSTANT'::text);

-- ----------------------------
-- View structure for "v_inp_evap_do"
-- ----------------------------
CREATE VIEW "v_inp_evap_do" AS 
SELECT 'DRY_ONLY'::text AS type_evdo, inp_evaporation.dry_only FROM inp_evaporation;

-- ----------------------------
-- View structure for "v_inp_evap_fl"
-- ----------------------------
CREATE VIEW "v_inp_evap_fl" AS 
SELECT inp_evaporation.evap_type AS type_evfl, inp_evaporation.pan_1, inp_evaporation.pan_2, inp_evaporation.pan_3, inp_evaporation.pan_4, inp_evaporation.pan_5, inp_evaporation.pan_6, inp_evaporation.pan_7, inp_evaporation.pan_8, inp_evaporation.pan_9, inp_evaporation.pan_10, inp_evaporation.pan_11, inp_evaporation.pan_12 FROM inp_evaporation WHERE ((inp_evaporation.evap_type)::text = 'FILE'::text);

-- ----------------------------
-- View structure for "v_inp_evap_mo"
-- ----------------------------
CREATE VIEW "v_inp_evap_mo" AS 
SELECT inp_evaporation.evap_type AS type_evmo, inp_evaporation.value_1, inp_evaporation.value_2, inp_evaporation.value_3, inp_evaporation.value_4, inp_evaporation.value_5, inp_evaporation.value_6, inp_evaporation.value_7, inp_evaporation.value_8, inp_evaporation.value_9, inp_evaporation.value_10, inp_evaporation.value_11, inp_evaporation.value_12 FROM inp_evaporation WHERE ((inp_evaporation.evap_type)::text = 'MONTHLY'::text);

-- ----------------------------
-- View structure for "v_inp_evap_pa"
-- ----------------------------
CREATE VIEW "v_inp_evap_pa" AS 
SELECT 'RECOVERY'::text AS type_evpa, inp_evaporation.recovery FROM inp_evaporation WHERE ((inp_evaporation.recovery)::text > '0'::text);

-- ----------------------------
-- View structure for "v_inp_evap_te"
-- ----------------------------
CREATE VIEW "v_inp_evap_te" AS 
SELECT inp_evaporation.evap_type AS type_evte FROM inp_evaporation WHERE ((inp_evaporation.evap_type)::text = 'TEMPERATURE'::text);

-- ----------------------------
-- View structure for "v_inp_evap_ts"
-- ----------------------------
CREATE VIEW "v_inp_evap_ts" AS 
SELECT inp_evaporation.evap_type AS type_evts, inp_evaporation.timser_id FROM inp_evaporation WHERE ((inp_evaporation.evap_type)::text = 'TIMESERIES'::text);

-- ----------------------------
-- View structure for "v_inp_groundwater"
-- ----------------------------
CREATE VIEW "v_inp_groundwater" AS 
SELECT inp_groundwater.subc_id, inp_groundwater.aquif_id, inp_groundwater.node_id, inp_groundwater.surfel, inp_groundwater.a1, inp_groundwater.b1, inp_groundwater.a2, inp_groundwater.b2, inp_groundwater.a3, inp_groundwater.tw, inp_groundwater.h, catch_selection.catch_id FROM ((subcatchment JOIN inp_groundwater ON ((inp_groundwater.subc_id = subcatchment.subc_id))) JOIN catch_selection ON ((subcatchment.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_hydrograph"
-- ----------------------------
CREATE VIEW "v_inp_hydrograph" AS 
SELECT inp_hydrograph.id, inp_hydrograph.text FROM inp_hydrograph ORDER BY inp_hydrograph.id;

-- ----------------------------
-- View structure for "v_inp_infiltration_cu"
-- ----------------------------
CREATE VIEW "v_inp_infiltration_cu" AS 
SELECT subcatchment.subc_id, subcatchment.curveno, subcatchment.conduct_2, subcatchment.drytime_2, catch_selection.catch_id, inp_options.infiltration FROM (subcatchment JOIN catch_selection ON (((subcatchment.catch_id)::text = (catch_selection.catch_id)::text))), inp_options WHERE ((inp_options.infiltration)::text = 'CURVE_NUMBER'::text);

-- ----------------------------
-- View structure for "v_inp_infiltration_gr"
-- ----------------------------
CREATE VIEW "v_inp_infiltration_gr" AS 
SELECT subcatchment.subc_id, subcatchment.suction, subcatchment.conduct, subcatchment.initdef, catch_selection.catch_id, inp_options.infiltration FROM (subcatchment JOIN catch_selection ON (((subcatchment.catch_id)::text = (catch_selection.catch_id)::text))), inp_options WHERE ((inp_options.infiltration)::text = 'GREEN_APT'::text);

-- ----------------------------
-- View structure for "v_inp_infiltration_ho"
-- ----------------------------
CREATE VIEW "v_inp_infiltration_ho" AS 
SELECT subcatchment.subc_id, subcatchment.maxrate, subcatchment.minrate, subcatchment.decay, subcatchment.drytime, subcatchment.maxinfil, catch_selection.catch_id, inp_options.infiltration FROM (subcatchment JOIN catch_selection ON (((subcatchment.catch_id)::text = (catch_selection.catch_id)::text))), inp_options WHERE ((inp_options.infiltration)::text = 'HORTON'::text);

-- ----------------------------
-- View structure for "v_inp_inflows_flow"
-- ----------------------------
CREATE VIEW "v_inp_inflows_flow" AS 
SELECT inp_inflows.node_id, 'FLOW'::text AS type_flow1, inp_inflows.timser_id, 'FLOW'::text AS type_flow2, '1'::text AS type_n1, inp_inflows.sfactor, inp_inflows.base, inp_inflows.patter_id, catch_selection.catch_id FROM ((catch_selection JOIN node ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_inflows ON ((inp_inflows.node_id = node.node_id)));

-- ----------------------------
-- View structure for "v_inp_inflows_load"
-- ----------------------------
CREATE VIEW "v_inp_inflows_load" AS 
SELECT inp_inflows_pol_x_node.poll_id, node.node_id, inp_inflows_pol_x_node.timser_id, inp_inflows_pol_x_node.form_type, inp_inflows_pol_x_node.mfactor, inp_inflows_pol_x_node.sfactor, inp_inflows_pol_x_node.base, inp_inflows_pol_x_node.patter_id, catch_selection.catch_id FROM ((catch_selection JOIN node ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_inflows_pol_x_node ON ((inp_inflows_pol_x_node.node_id = node.node_id)));

-- ----------------------------
-- View structure for "v_inp_junction"
-- ----------------------------
CREATE VIEW "v_inp_junction" AS 
SELECT node.node_id, node.elev, node.ymax, junction.y0, junction.ysur, junction.apond, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((inp_junction junction JOIN node ON ((junction.node_id = node.node_id))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_landuses"
-- ----------------------------
CREATE VIEW "v_inp_landuses" AS 
SELECT inp_landuses.landus_id, inp_landuses.sweepint, inp_landuses.availab, inp_landuses.lastsweep FROM inp_landuses;

-- ----------------------------
-- View structure for "v_inp_lidcontrol"
-- ----------------------------
CREATE VIEW "v_inp_lidcontrol" AS 
SELECT inp_lid_control.lidco_id, inp_lid_control.lidco_type, inp_lid_control.value_2, inp_lid_control.value_3, inp_lid_control.value_4, inp_lid_control.value_5, inp_lid_control.value_6, inp_lid_control.value_7, inp_lid_control.value_8 FROM inp_lid_control ORDER BY inp_lid_control.id;

-- ----------------------------
-- View structure for "v_inp_lidusage"
-- ----------------------------
CREATE VIEW "v_inp_lidusage" AS 
SELECT inp_lidusage_subc_x_lidco.subc_id, inp_lidusage_subc_x_lidco.lidco_id, inp_lidusage_subc_x_lidco.number, inp_lidusage_subc_x_lidco.area, inp_lidusage_subc_x_lidco.width, inp_lidusage_subc_x_lidco.initsat, inp_lidusage_subc_x_lidco.fromimp, inp_lidusage_subc_x_lidco.toperv, inp_lidusage_subc_x_lidco.rptfile, catch_selection.catch_id FROM ((catch_selection JOIN subcatchment ON ((subcatchment.catch_id = catch_selection.catch_id))) JOIN inp_lidusage_subc_x_lidco ON ((inp_lidusage_subc_x_lidco.subc_id = subcatchment.subc_id)));

-- ----------------------------
-- View structure for "v_inp_loadings"
-- ----------------------------
CREATE VIEW "v_inp_loadings" AS 
SELECT inp_loadings_pol_x_subc.poll_id, inp_loadings_pol_x_subc.subc_id, inp_loadings_pol_x_subc.ibuilddup AS ib, catch_selection.catch_id FROM ((catch_selection JOIN subcatchment ON ((subcatchment.catch_id = catch_selection.catch_id))) JOIN inp_loadings_pol_x_subc ON ((inp_loadings_pol_x_subc.subc_id = subcatchment.subc_id)));

-- ----------------------------
-- View structure for "v_inp_losses"
-- ----------------------------
CREATE VIEW "v_inp_losses" AS 
SELECT inp_conduit.arc_id, inp_conduit.kentry, inp_conduit.kexit, inp_conduit.kavg, inp_conduit.flap, catch_selection.catch_id FROM ((inp_conduit JOIN arc ON ((inp_conduit.arc_id = arc.arc_id))) JOIN catch_selection ON ((arc.catch_id = catch_selection.catch_id))) WHERE ((((inp_conduit.kentry > (0)::numeric) OR (inp_conduit.kexit > (0)::numeric)) OR (inp_conduit.kavg > (0)::numeric)) OR ((inp_conduit.flap)::text = 'YES'::text));

-- ----------------------------
-- View structure for "v_inp_orifice"
-- ----------------------------
CREATE VIEW "v_inp_orifice" AS 
SELECT inp_orifice.arc_id, arc.node_1, arc.node_2, inp_orifice.ori_type, inp_orifice."offset", inp_orifice.cd, inp_orifice.flap, inp_orifice.orate, inp_orifice.shape, inp_orifice.geom1, inp_orifice.geom2, inp_orifice.geom3, inp_orifice.geom4, catch_selection.catch_id FROM ((arc JOIN inp_orifice ON ((inp_orifice.arc_id = arc.arc_id))) JOIN catch_selection ON ((arc.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_outfall_fi"
-- ----------------------------
CREATE VIEW "v_inp_outfall_fi" AS 
SELECT node.node_id, node.elev, inp_outfall.outfall_type AS type_otlfi, inp_outfall.stage, inp_outfall.gate, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_outfall ON ((inp_outfall.node_id = node.node_id))) WHERE ((inp_outfall.outfall_type)::text = 'FIXED'::text);

-- ----------------------------
-- View structure for "v_inp_outfall_fr"
-- ----------------------------
CREATE VIEW "v_inp_outfall_fr" AS 
SELECT node.node_id, node.elev, inp_outfall.outfall_type AS type_otlfr, inp_outfall.gate, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN inp_outfall ON (((node.node_id)::text = (inp_outfall.node_id)::text))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) WHERE ((inp_outfall.outfall_type)::text = 'FREE'::text);

-- ----------------------------
-- View structure for "v_inp_outfall_nm"
-- ----------------------------
CREATE VIEW "v_inp_outfall_nm" AS 
SELECT node.node_id, node.elev, inp_outfall.outfall_type AS type_otlnm, inp_outfall.gate, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_outfall ON ((node.node_id = inp_outfall.node_id))) WHERE ((inp_outfall.outfall_type)::text = 'NORMAL'::text);

-- ----------------------------
-- View structure for "v_inp_outfall_ti"
-- ----------------------------
CREATE VIEW "v_inp_outfall_ti" AS 
SELECT node.node_id, node.elev, inp_outfall.outfall_type AS type_otlti, inp_outfall.curve_id, inp_outfall.gate, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_outfall ON ((node.node_id = inp_outfall.node_id))) WHERE ((inp_outfall.outfall_type)::text = 'TIDAL'::text);

-- ----------------------------
-- View structure for "v_inp_outfall_ts"
-- ----------------------------
CREATE VIEW "v_inp_outfall_ts" AS 
SELECT node.node_id, node.elev, inp_outfall.outfall_type AS type_otlts, inp_outfall.timser_id, inp_outfall.gate, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_outfall ON ((node.node_id = inp_outfall.node_id))) WHERE ((inp_outfall.outfall_type)::text = 'TIMESERIES'::text);

-- ----------------------------
-- View structure for "v_inp_outlet_fcd"
-- ----------------------------
CREATE VIEW "v_inp_outlet_fcd" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, inp_outlet.outlet_type AS type_oufcd, inp_outlet."offset", inp_outlet.cd1, inp_outlet.cd2, inp_outlet.flap, catch_selection.catch_id FROM ((arc JOIN inp_outlet ON ((((arc.arc_id)::text = (inp_outlet.arc_id)::text) AND ((arc.arc_id)::text = (inp_outlet.arc_id)::text)))) JOIN catch_selection ON (((arc.catch_id)::text = (catch_selection.catch_id)::text))) WHERE ((inp_outlet.outlet_type)::text = 'FUNCTIONAL/DEPTH'::text);

-- ----------------------------
-- View structure for "v_inp_outlet_fch"
-- ----------------------------
CREATE VIEW "v_inp_outlet_fch" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, inp_outlet.outlet_type AS type_oufch, inp_outlet."offset", inp_outlet.cd1, inp_outlet.cd2, inp_outlet.flap, catch_selection.catch_id FROM ((arc JOIN inp_outlet ON (((arc.arc_id)::text = (inp_outlet.arc_id)::text))) JOIN catch_selection ON (((arc.catch_id)::text = (catch_selection.catch_id)::text))) WHERE ((inp_outlet.outlet_type)::text = 'FUNCTIONAL/HEAD'::text);

-- ----------------------------
-- View structure for "v_inp_outlet_tbd"
-- ----------------------------
CREATE VIEW "v_inp_outlet_tbd" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, inp_outlet.outlet_type AS type_outbd, inp_outlet."offset", inp_outlet.curve_id, inp_outlet.flap, catch_selection.catch_id FROM ((arc JOIN inp_outlet ON (((arc.arc_id)::text = (inp_outlet.arc_id)::text))) JOIN catch_selection ON (((arc.catch_id)::text = (catch_selection.catch_id)::text))) WHERE ((inp_outlet.outlet_type)::text = 'TABULAR/DEPTH'::text);

-- ----------------------------
-- View structure for "v_inp_outlet_tbh"
-- ----------------------------
CREATE VIEW "v_inp_outlet_tbh" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, inp_outlet.outlet_type AS type_outbh, inp_outlet."offset", inp_outlet.curve_id, inp_outlet.flap, catch_selection.catch_id FROM ((arc JOIN inp_outlet ON (((arc.arc_id)::text = (inp_outlet.arc_id)::text))) JOIN catch_selection ON (((arc.catch_id)::text = (catch_selection.catch_id)::text))) WHERE ((inp_outlet.outlet_type)::text = 'TABULAR/HEAD'::text);

-- ----------------------------
-- View structure for "v_inp_pattern_dl"
-- ----------------------------
CREATE VIEW "v_inp_pattern_dl" AS 
SELECT inp_pattern.patter_id, inp_pattern.patter_type AS type_padl, inp_pattern.factor_1, inp_pattern.factor_2, inp_pattern.factor_3, inp_pattern.factor_4, inp_pattern.factor_5, inp_pattern.factor_6, inp_pattern.factor_7 FROM inp_pattern WHERE ((inp_pattern.patter_type)::text = 'DAILY'::text);

-- ----------------------------
-- View structure for "v_inp_pattern_ho"
-- ----------------------------
CREATE VIEW "v_inp_pattern_ho" AS 
SELECT inp_pattern.patter_id, inp_pattern.patter_type AS type_paho, inp_pattern.factor_1, inp_pattern.factor_2, inp_pattern.factor_3, inp_pattern.factor_4, inp_pattern.factor_5, inp_pattern.factor_6, inp_pattern.factor_7, inp_pattern.factor_8, inp_pattern.factor_9, inp_pattern.factor_10, inp_pattern.factor_11, inp_pattern.factor_12, inp_pattern.factor_13, inp_pattern.factor_14, inp_pattern.factor_15, inp_pattern.factor_16, inp_pattern.factor_17, inp_pattern.factor_18, inp_pattern.factor_19, inp_pattern.factor_20, inp_pattern.factor_21, inp_pattern.factor_22, inp_pattern.factor_23, inp_pattern.factor_24 FROM inp_pattern WHERE ((inp_pattern.patter_type)::text = 'HOURLY'::text);

-- ----------------------------
-- View structure for "v_inp_pattern_mo"
-- ----------------------------
CREATE VIEW "v_inp_pattern_mo" AS 
SELECT inp_pattern.patter_id, inp_pattern.patter_type AS type_pamo, inp_pattern.factor_1, inp_pattern.factor_2, inp_pattern.factor_3, inp_pattern.factor_4, inp_pattern.factor_5, inp_pattern.factor_6, inp_pattern.factor_7, inp_pattern.factor_8, inp_pattern.factor_9, inp_pattern.factor_10, inp_pattern.factor_11, inp_pattern.factor_12 FROM inp_pattern WHERE ((inp_pattern.patter_type)::text = 'MONTHLY'::text);

-- ----------------------------
-- View structure for "v_inp_pattern_we"
-- ----------------------------
CREATE VIEW "v_inp_pattern_we" AS 
SELECT inp_pattern.patter_id, inp_pattern.patter_type AS type_pawe, inp_pattern.factor_1, inp_pattern.factor_2, inp_pattern.factor_3, inp_pattern.factor_4, inp_pattern.factor_5, inp_pattern.factor_6, inp_pattern.factor_7, inp_pattern.factor_8, inp_pattern.factor_9, inp_pattern.factor_10, inp_pattern.factor_11, inp_pattern.factor_12, inp_pattern.factor_13, inp_pattern.factor_14, inp_pattern.factor_15, inp_pattern.factor_16, inp_pattern.factor_17, inp_pattern.factor_18, inp_pattern.factor_19, inp_pattern.factor_20, inp_pattern.factor_21, inp_pattern.factor_22, inp_pattern.factor_23, inp_pattern.factor_24 FROM inp_pattern WHERE ((inp_pattern.patter_type)::text = 'WEEKEND'::text);

-- ----------------------------
-- View structure for "v_inp_pump"
-- ----------------------------
CREATE VIEW "v_inp_pump" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, inp_pump.curve_id, inp_pump.status, inp_pump.startup, inp_pump.shutoff, catch_selection.catch_id FROM ((arc JOIN inp_pump ON ((arc.arc_id = inp_pump.arc_id))) JOIN catch_selection ON ((arc.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_rdii"
-- ----------------------------
CREATE VIEW "v_inp_rdii" AS 
SELECT node.node_id, inp_rdii.hydro_id, inp_rdii.sewerarea, catch_selection.catch_id FROM ((catch_selection JOIN node ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_rdii ON ((inp_rdii.node_id = node.node_id)));

-- ----------------------------
-- View structure for "v_inp_rgage_fl"
-- ----------------------------
CREATE VIEW "v_inp_rgage_fl" AS 
SELECT raingage.rg_id, raingage.form_type, raingage.intvl, raingage.scf, raingage.rgage_type AS type_rgfl, raingage.fname, raingage.sta, raingage.units, (st_x(raingage.the_geom))::numeric(16,3) AS xcoord, (st_y(raingage.the_geom))::numeric(16,3) AS ycoord FROM raingage WHERE ((raingage.rgage_type)::text = 'FILE'::text);

-- ----------------------------
-- View structure for "v_inp_rgage_ts"
-- ----------------------------
CREATE VIEW "v_inp_rgage_ts" AS 
SELECT raingage.rg_id, raingage.form_type, raingage.intvl, raingage.scf, raingage.rgage_type AS type_rgts, raingage.timser_id, (st_x(raingage.the_geom))::numeric(16,3) AS xcoord, (st_y(raingage.the_geom))::numeric(16,3) AS ycoord FROM raingage WHERE ((raingage.rgage_type)::text = 'TIMESERIES'::text);

-- ----------------------------
-- View structure for "v_inp_snowpack"
-- ----------------------------
CREATE VIEW "v_inp_snowpack" AS 
SELECT inp_snowpack.snow_id, 'PLOWABLE'::text AS type_snpk1, inp_snowpack.cmin_1, inp_snowpack.cmax_1, inp_snowpack.tbase_1, inp_snowpack.fwf_1, inp_snowpack.sd0_1, inp_snowpack.fw0_1, inp_snowpack.smn0_1, 'IMPERVIOUS'::text AS type_snpk2, inp_snowpack.cmin_2, inp_snowpack.cmax_2, inp_snowpack.tbase_2, inp_snowpack.fwf_2, inp_snowpack.sd0_2, inp_snowpack.fw0_2, inp_snowpack.sd100_1, 'PERVIOUS'::text AS type_snpk3, inp_snowpack.cmin_3, inp_snowpack.cmax_3, inp_snowpack.tbase_3, inp_snowpack.fwf_3, inp_snowpack.sd0_3, inp_snowpack.fw0_3, inp_snowpack.sd100_2, 'REMOVAL'::text AS type_snpk4, inp_snowpack.dplow, inp_snowpack.fout, inp_snowpack.fimp, inp_snowpack.fperv, inp_snowpack.fimelt, inp_snowpack.fsub, inp_snowpack.subc_id FROM inp_snowpack;

-- ----------------------------
-- View structure for "v_inp_storage_fc"
-- ----------------------------
CREATE VIEW "v_inp_storage_fc" AS 
SELECT node.node_id, node.elev, node.ymax, node.y0, inp_storage.storage_type AS type_stfc, inp_storage.a1, inp_storage.a2, inp_storage.a0, node.apond, inp_storage.fevap, inp_storage.sh, inp_storage.hc, inp_storage.imd, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN inp_storage ON (((node.node_id)::text = (inp_storage.node_id)::text))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) WHERE ((inp_storage.storage_type)::text = 'FUNCTIONAL'::text);

-- ----------------------------
-- View structure for "v_inp_storage_tb"
-- ----------------------------
CREATE VIEW "v_inp_storage_tb" AS 
SELECT node.node_id, node.elev, node.ymax, node.y0, inp_storage.storage_type AS type_sttb, inp_storage.curve_id, node.apond, inp_storage.fevap, inp_storage.sh, inp_storage.hc, inp_storage.imd, (st_x(node.the_geom))::numeric(16,3) AS xcoord, (st_y(node.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((node JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id))) JOIN inp_storage ON ((node.node_id = inp_storage.node_id))) WHERE ((inp_storage.storage_type)::text = 'TABULAR'::text);

-- ----------------------------
-- View structure for "v_inp_subcatch"
-- ----------------------------
CREATE VIEW "v_inp_subcatch" AS 
SELECT subcatchment.subc_id, subcatchment.node_id, subcatchment.rg_id, subcatchment.area, subcatchment.imperv, subcatchment.width, subcatchment.slope, subcatchment.clength, subcatchment.snow_id, subcatchment.nimp, subcatchment.nperv, subcatchment.simp, subcatchment.sperv, subcatchment.zero, subcatchment.routeto, subcatchment.rted, catch_selection.catch_id FROM (subcatchment JOIN catch_selection ON ((subcatchment.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_temp_fl"
-- ----------------------------
CREATE VIEW "v_inp_temp_fl" AS 
SELECT inp_temperature.temp_type AS type_tefl, inp_temperature.fname, inp_temperature.start FROM inp_temperature WHERE ((inp_temperature.temp_type)::text = 'FILE'::text);

-- ----------------------------
-- View structure for "v_inp_temp_sn"
-- ----------------------------
CREATE VIEW "v_inp_temp_sn" AS 
SELECT 'SNOWMELT'::text AS type_tesn, inp_snowmelt.stemp, inp_snowmelt.atiwt, inp_snowmelt.rnm, inp_snowmelt.elev, inp_snowmelt.lat, inp_snowmelt.dtlong, 'ADC IMPERVIOUS'::text AS type_teai, inp_snowmelt.i_f0, inp_snowmelt.i_f1, inp_snowmelt.i_f2, inp_snowmelt.i_f3, inp_snowmelt.i_f4, inp_snowmelt.i_f5, inp_snowmelt.i_f6, inp_snowmelt.i_f7, inp_snowmelt.i_f8, inp_snowmelt.i_f9, 'ADC PERVIOUS'::text AS type_teap, inp_snowmelt.p_f0, inp_snowmelt.p_f1, inp_snowmelt.p_f2, inp_snowmelt.p_f3, inp_snowmelt.p_f4, inp_snowmelt.p_f5, inp_snowmelt.p_f6, inp_snowmelt.p_f7, inp_snowmelt.p_f8, inp_snowmelt.p_f9 FROM inp_snowmelt;

-- ----------------------------
-- View structure for "v_inp_temp_ts"
-- ----------------------------
CREATE VIEW "v_inp_temp_ts" AS 
SELECT inp_temperature.temp_type AS type_tets, inp_temperature.timser_id FROM inp_temperature WHERE ((inp_temperature.temp_type)::text = 'TIMESERIES'::text);

-- ----------------------------
-- View structure for "v_inp_temp_wf"
-- ----------------------------
CREATE VIEW "v_inp_temp_wf" AS 
SELECT 'WINDSPEED'::text AS type_tews, inp_windspeed.wind_type AS type_tefl, inp_windspeed.fname FROM inp_windspeed WHERE ((inp_windspeed.wind_type)::text = 'FILE'::text);

-- ----------------------------
-- View structure for "v_inp_temp_wm"
-- ----------------------------
CREATE VIEW "v_inp_temp_wm" AS 
SELECT 'WINDSPEED'::text AS type_tews, inp_windspeed.wind_type AS type_temo, inp_windspeed.value_1, inp_windspeed.value_2, inp_windspeed.value_3, inp_windspeed.value_4, inp_windspeed.value_5, inp_windspeed.value_6, inp_windspeed.value_7, inp_windspeed.value_8, inp_windspeed.value_9, inp_windspeed.value_10, inp_windspeed.value_11, inp_windspeed.value_12 FROM inp_windspeed WHERE ((inp_windspeed.wind_type)::text = 'MONTHLY'::text);

-- ----------------------------
-- View structure for "v_inp_timser_abs"
-- ----------------------------
CREATE VIEW "v_inp_timser_abs" AS 
SELECT inp_timeseries.timser_id, inp_timeseries.date, inp_timeseries.hour, inp_timeseries.value FROM inp_timeseries WHERE ((inp_timeseries.times_type)::text = 'ABSOLUTE'::text) ORDER BY inp_timeseries.id;

-- ----------------------------
-- View structure for "v_inp_timser_fl"
-- ----------------------------
CREATE VIEW "v_inp_timser_fl" AS 
SELECT inp_timeseries.timser_id, 'FILE'::text AS type_times, inp_timeseries.fname FROM inp_timeseries WHERE ((inp_timeseries.times_type)::text = 'FILE'::text);

-- ----------------------------
-- View structure for "v_inp_timser_rel"
-- ----------------------------
CREATE VIEW "v_inp_timser_rel" AS 
SELECT inp_timeseries.timser_id, inp_timeseries."time", inp_timeseries.value FROM inp_timeseries WHERE ((inp_timeseries.times_type)::text = 'RELATIVE'::text) ORDER BY inp_timeseries.id;

-- ----------------------------
-- View structure for "v_inp_transects"
-- ----------------------------
CREATE VIEW "v_inp_transects" AS 
SELECT inp_transects.id, inp_transects.text FROM inp_transects ORDER BY inp_transects.id;

-- ----------------------------
-- View structure for "v_inp_treatment"
-- ----------------------------
CREATE VIEW "v_inp_treatment" AS 
SELECT node.node_id, inp_treatment_node_x_pol.poll_id, inp_treatment_node_x_pol.function, catch_selection.catch_id FROM ((node JOIN inp_treatment_node_x_pol ON ((inp_treatment_node_x_pol.node_id = node.node_id))) JOIN catch_selection ON ((node.catch_id = catch_selection.catch_id)));

-- ----------------------------
-- View structure for "v_inp_vertice"
-- ----------------------------
CREATE VIEW "v_inp_vertice" AS 
SELECT vertice.vertice_id, vertice.arc_id, (st_x(vertice.the_geom))::numeric(16,3) AS xcoord, (st_y(vertice.the_geom))::numeric(16,3) AS ycoord, catch_selection.catch_id FROM ((arc JOIN catch_selection ON ((arc.catch_id = catch_selection.catch_id))) JOIN vertice ON ((vertice.arc_id = arc.arc_id))) ORDER BY vertice.vertice_id;

-- ----------------------------
-- View structure for "v_inp_washoff"
-- ----------------------------
CREATE VIEW "v_inp_washoff" AS 
SELECT inp_washoff_land_x_pol.landus_id, inp_washoff_land_x_pol.poll_id, inp_washoff_land_x_pol.funcw_type, inp_washoff_land_x_pol.c1, inp_washoff_land_x_pol.c2, inp_washoff_land_x_pol.sweepeffic, inp_washoff_land_x_pol.bmpeffic FROM inp_washoff_land_x_pol;

-- ----------------------------
-- View structure for "v_inp_weir"
-- ----------------------------
CREATE VIEW "v_inp_weir" AS 
SELECT arc.arc_id, arc.node_1, arc.node_2, inp_weir.weir_type, inp_weir."offset", inp_weir.cd, inp_weir.flap, inp_weir.ec, inp_weir.cd2, inp_value_weirs.shape, inp_weir.geom1, inp_weir.geom2, inp_weir.geom3, inp_weir.geom4, catch_selection.catch_id FROM (((arc JOIN catch_selection ON ((arc.catch_id = catch_selection.catch_id))) JOIN inp_weir ON ((inp_weir.arc_id = arc.arc_id))) JOIN inp_value_weirs ON (((inp_weir.weir_type)::text = (inp_value_weirs.id)::text)));

-- ----------------------------
-- View structure for "v_man_arc"
-- ----------------------------
CREATE VIEW "v_man_arc" AS 
SELECT arc.arc_id, round((st_length2d(arc.the_geom))::numeric, 2) AS longitud, arc.y1 AS prof_ini, arc.y2 AS prof_fin, arc.arc_slope AS pendiente, cat_mat.descript AS material, cat_arc.shape AS forma, cat_arc.geom1, cat_arc.geom2, arc.label AS etiqueta, arc.catch_id AS sector, arc.categ_type AS categoria, arc.systm_type AS tipo_red, man_arcdat.visitable, man_arcdat.builddate AS fechacons, man_event.date AS inspeccion, man_event_x_arc.sed_lev AS est_sedimentos, man_event_x_arc.bott_sta AS est_cubeta, man_event_x_arc.mid_sta AS est_hastiales, man_event_x_arc.top_sta AS est_clave, arc.link, arc.direction, arc.the_geom FROM (((((arc JOIN man_arcdat ON ((arc.arc_id = man_arcdat.arc_id))) JOIN man_event_x_arc ON ((man_arcdat.arc_id = man_event_x_arc.arc_id))) JOIN man_event ON ((man_event_x_arc.event_id = man_event.id))) JOIN cat_mat ON (((cat_mat.id)::text = (arc.matcat_id)::text))) JOIN cat_arc ON (((cat_arc.id)::text = (arc.arccat_id)::text)));

-- ----------------------------
-- View structure for "v_man_node"
-- ----------------------------
CREATE VIEW "v_man_node" AS 
SELECT node.node_id, node.top_elev AS cota_sup, node.elev AS cota_inf, node.ymax AS profunidad, node.catch_id AS sector, node.node_type AS tipo_nodo, cat_cover.short_des AS tapa_registro, man_manhole.build_date AS fechacons, man_event.date AS inspeccion, man_event_x_node.sed_lev AS sedimentos, man_event_x_node.bott_sta AS est_cubeta, man_event_x_node.wall_sta AS est_paredes, man_event_x_node.top_sta AS est_clave, man_event_x_node.covesta_id AS est_tapa, man_event_x_node.step_num AS pates_a_reponer, node.link, node.the_geom FROM ((((node JOIN man_manhole ON ((node.node_id = man_manhole.node_id))) JOIN man_event_x_node ON ((man_manhole.node_id = man_event_x_node.node_id))) JOIN man_event ON ((man_event_x_node.event_id = man_event.id))) JOIN cat_cover ON (((cat_cover.id)::text = (man_manhole.covcat_id)::text)));

-- ----------------------------
-- View structure for "v_rpt_arcflow_sum"
-- ----------------------------
CREATE VIEW "v_rpt_arcflow_sum" AS 
SELECT result_selection.result_id, rpt_arcflow_sum.arc_id, rpt_arcflow_sum.arc_type, rpt_arcflow_sum.max_flow, rpt_arcflow_sum.time_days, rpt_arcflow_sum.time_hour, rpt_arcflow_sum.max_veloc, rpt_arcflow_sum.mfull_flow, rpt_arcflow_sum.mfull_dept, arc.the_geom FROM ((arc JOIN rpt_arcflow_sum ON (((rpt_arcflow_sum.arc_id = arc.arc_id) AND (arc.arc_id = rpt_arcflow_sum.arc_id)))) JOIN result_selection ON (((rpt_arcflow_sum.result_id)::text = (result_selection.result_id)::text)));

-- ----------------------------
-- View structure for "v_rpt_nodeflood_sum"
-- ----------------------------
CREATE VIEW "v_rpt_nodeflood_sum" AS 
SELECT result_selection.result_id, rpt_nodeflooding_sum.node_id, rpt_nodeflooding_sum.hour_flood, rpt_nodeflooding_sum.max_rate, rpt_nodeflooding_sum.time_days, rpt_nodeflooding_sum.time_hour, rpt_nodeflooding_sum.tot_flood, rpt_nodeflooding_sum.max_ponded, node.the_geom FROM ((node JOIN rpt_nodeflooding_sum ON ((rpt_nodeflooding_sum.node_id = node.node_id))) JOIN result_selection ON (((rpt_nodeflooding_sum.result_id)::text = (result_selection.result_id)::text)));

-- ----------------------------
-- Primary Key structure for table "arc"
-- ----------------------------
ALTER TABLE "arc" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "cat_arc"
-- ----------------------------
ALTER TABLE "cat_arc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "cat_connec"
-- ----------------------------
ALTER TABLE "cat_connec" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "cat_cover"
-- ----------------------------
ALTER TABLE "cat_cover" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "cat_gully"
-- ----------------------------
ALTER TABLE "cat_gully" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "cat_mat"
-- ----------------------------
ALTER TABLE "cat_mat" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "cat_mhole"
-- ----------------------------
ALTER TABLE "cat_mhole" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "catch_selection"
-- ----------------------------
ALTER TABLE "catch_selection" ADD PRIMARY KEY ("catch_id");

-- ----------------------------
-- Primary Key structure for table "catchment"
-- ----------------------------
ALTER TABLE "catchment" ADD PRIMARY KEY ("catch_id");

-- ----------------------------
-- Primary Key structure for table "connec"
-- ----------------------------
ALTER TABLE "connec" ADD PRIMARY KEY ("connec_id");

-- ----------------------------
-- Primary Key structure for table "gully"
-- ----------------------------
ALTER TABLE "gully" ADD PRIMARY KEY ("gully_id");

-- ----------------------------
-- Primary Key structure for table "inp_aquifer"
-- ----------------------------
ALTER TABLE "inp_aquifer" ADD PRIMARY KEY ("aquif_id");

-- ----------------------------
-- Primary Key structure for table "inp_backdrop"
-- ----------------------------
ALTER TABLE "inp_backdrop" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_buildup_land_x_pol"
-- ----------------------------
ALTER TABLE "inp_buildup_land_x_pol" ADD PRIMARY KEY ("landus_id", "poll_id");

-- ----------------------------
-- Primary Key structure for table "inp_conduit"
-- ----------------------------
ALTER TABLE "inp_conduit" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "inp_controls"
-- ----------------------------
ALTER TABLE "inp_controls" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_coverage_land_x_subc"
-- ----------------------------
ALTER TABLE "inp_coverage_land_x_subc" ADD PRIMARY KEY ("subc_id", "landus_id");

-- ----------------------------
-- Primary Key structure for table "inp_curve"
-- ----------------------------
ALTER TABLE "inp_curve" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_curve_id"
-- ----------------------------
ALTER TABLE "inp_curve_id" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_divider"
-- ----------------------------
ALTER TABLE "inp_divider" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "inp_dwf"
-- ----------------------------
ALTER TABLE "inp_dwf" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_dwf_pol_x_node"
-- ----------------------------
ALTER TABLE "inp_dwf_pol_x_node" ADD PRIMARY KEY ("poll_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "inp_evaporation"
-- ----------------------------
ALTER TABLE "inp_evaporation" ADD PRIMARY KEY ("evap_type");

-- ----------------------------
-- Primary Key structure for table "inp_files"
-- ----------------------------
ALTER TABLE "inp_files" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_groundwater"
-- ----------------------------
ALTER TABLE "inp_groundwater" ADD PRIMARY KEY ("subc_id", "aquif_id");

-- ----------------------------
-- Primary Key structure for table "inp_hydrograph"
-- ----------------------------
ALTER TABLE "inp_hydrograph" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_inflows"
-- ----------------------------
ALTER TABLE "inp_inflows" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_inflows_pol_x_node"
-- ----------------------------
ALTER TABLE "inp_inflows_pol_x_node" ADD PRIMARY KEY ("poll_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "inp_junction"
-- ----------------------------
ALTER TABLE "inp_junction" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "inp_label"
-- ----------------------------
ALTER TABLE "inp_label" ADD PRIMARY KEY ("label");

-- ----------------------------
-- Primary Key structure for table "inp_landuses"
-- ----------------------------
ALTER TABLE "inp_landuses" ADD PRIMARY KEY ("landus_id");

-- ----------------------------
-- Primary Key structure for table "inp_lid_control"
-- ----------------------------
ALTER TABLE "inp_lid_control" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_lidusage_subc_x_lidco"
-- ----------------------------
ALTER TABLE "inp_lidusage_subc_x_lidco" ADD PRIMARY KEY ("subc_id", "lidco_id");

-- ----------------------------
-- Primary Key structure for table "inp_loadings_pol_x_subc"
-- ----------------------------
ALTER TABLE "inp_loadings_pol_x_subc" ADD PRIMARY KEY ("poll_id", "subc_id");

-- ----------------------------
-- Primary Key structure for table "inp_options"
-- ----------------------------
ALTER TABLE "inp_options" ADD PRIMARY KEY ("flow_units");

-- ----------------------------
-- Primary Key structure for table "inp_orifice"
-- ----------------------------
ALTER TABLE "inp_orifice" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "inp_outfall"
-- ----------------------------
ALTER TABLE "inp_outfall" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "inp_outlet"
-- ----------------------------
ALTER TABLE "inp_outlet" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "inp_pattern"
-- ----------------------------
ALTER TABLE "inp_pattern" ADD PRIMARY KEY ("patter_id");

-- ----------------------------
-- Primary Key structure for table "inp_pollutant"
-- ----------------------------
ALTER TABLE "inp_pollutant" ADD PRIMARY KEY ("poll_id");

-- ----------------------------
-- Primary Key structure for table "inp_pump"
-- ----------------------------
ALTER TABLE "inp_pump" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "inp_rdii"
-- ----------------------------
ALTER TABLE "inp_rdii" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "inp_report"
-- ----------------------------
ALTER TABLE "inp_report" ADD PRIMARY KEY ("input");

-- ----------------------------
-- Primary Key structure for table "inp_snowmelt"
-- ----------------------------
ALTER TABLE "inp_snowmelt" ADD PRIMARY KEY ("stemp");

-- ----------------------------
-- Primary Key structure for table "inp_snowpack"
-- ----------------------------
ALTER TABLE "inp_snowpack" ADD PRIMARY KEY ("snow_id");

-- ----------------------------
-- Primary Key structure for table "inp_storage"
-- ----------------------------
ALTER TABLE "inp_storage" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "inp_temperature"
-- ----------------------------
ALTER TABLE "inp_temperature" ADD PRIMARY KEY ("temp_type");

-- ----------------------------
-- Primary Key structure for table "inp_timeseries"
-- ----------------------------
ALTER TABLE "inp_timeseries" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_timser_id"
-- ----------------------------
ALTER TABLE "inp_timser_id" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_transects"
-- ----------------------------
ALTER TABLE "inp_transects" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_treatment_node_x_pol"
-- ----------------------------
ALTER TABLE "inp_treatment_node_x_pol" ADD PRIMARY KEY ("node_id", "poll_id");

-- ----------------------------
-- Primary Key structure for table "inp_type_arc"
-- ----------------------------
ALTER TABLE "inp_type_arc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_type_node"
-- ----------------------------
ALTER TABLE "inp_type_node" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_divider"
-- ----------------------------
ALTER TABLE "inp_typevalue_divider" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_evap"
-- ----------------------------
ALTER TABLE "inp_typevalue_evap" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_outfall"
-- ----------------------------
ALTER TABLE "inp_typevalue_outfall" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_outlet"
-- ----------------------------
ALTER TABLE "inp_typevalue_outlet" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_pattern"
-- ----------------------------
ALTER TABLE "inp_typevalue_pattern" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_raingage"
-- ----------------------------
ALTER TABLE "inp_typevalue_raingage" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_storage"
-- ----------------------------
ALTER TABLE "inp_typevalue_storage" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_temp"
-- ----------------------------
ALTER TABLE "inp_typevalue_temp" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_timeseries"
-- ----------------------------
ALTER TABLE "inp_typevalue_timeseries" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_typevalue_windsp"
-- ----------------------------
ALTER TABLE "inp_typevalue_windsp" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_allnone"
-- ----------------------------
ALTER TABLE "inp_value_allnone" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_buildup"
-- ----------------------------
ALTER TABLE "inp_value_buildup" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_curve"
-- ----------------------------
ALTER TABLE "inp_value_curve" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_files_actio"
-- ----------------------------
ALTER TABLE "inp_value_files_actio" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_files_type"
-- ----------------------------
ALTER TABLE "inp_value_files_type" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_lidcontrol"
-- ----------------------------
ALTER TABLE "inp_value_lidcontrol" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_mapunits"
-- ----------------------------
ALTER TABLE "inp_value_mapunits" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_fme"
-- ----------------------------
ALTER TABLE "inp_value_options_fme" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_fr"
-- ----------------------------
ALTER TABLE "inp_value_options_fr" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_fu"
-- ----------------------------
ALTER TABLE "inp_value_options_fu" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_id"
-- ----------------------------
ALTER TABLE "inp_value_options_id" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_in"
-- ----------------------------
ALTER TABLE "inp_value_options_in" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_lo"
-- ----------------------------
ALTER TABLE "inp_value_options_lo" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_options_nfl"
-- ----------------------------
ALTER TABLE "inp_value_options_nfl" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_orifice"
-- ----------------------------
ALTER TABLE "inp_value_orifice" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_raingage"
-- ----------------------------
ALTER TABLE "inp_value_raingage" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_timserid"
-- ----------------------------
ALTER TABLE "inp_value_timserid" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_washoff"
-- ----------------------------
ALTER TABLE "inp_value_washoff" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_weirs"
-- ----------------------------
ALTER TABLE "inp_value_weirs" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_value_yesno"
-- ----------------------------
ALTER TABLE "inp_value_yesno" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "inp_washoff_land_x_pol"
-- ----------------------------
ALTER TABLE "inp_washoff_land_x_pol" ADD PRIMARY KEY ("landus_id", "poll_id");

-- ----------------------------
-- Primary Key structure for table "inp_weir"
-- ----------------------------
ALTER TABLE "inp_weir" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "inp_windspeed"
-- ----------------------------
ALTER TABLE "inp_windspeed" ADD PRIMARY KEY ("wind_type");

-- ----------------------------
-- Primary Key structure for table "man_arcdat"
-- ----------------------------
ALTER TABLE "man_arcdat" ADD PRIMARY KEY ("arc_id");

-- ----------------------------
-- Primary Key structure for table "man_element"
-- ----------------------------
ALTER TABLE "man_element" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "man_event"
-- ----------------------------
ALTER TABLE "man_event" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_event_x_arc"
-- ----------------------------
ALTER TABLE "man_event_x_arc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_event_x_connec"
-- ----------------------------
ALTER TABLE "man_event_x_connec" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_event_x_gully"
-- ----------------------------
ALTER TABLE "man_event_x_gully" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_event_x_node"
-- ----------------------------
ALTER TABLE "man_event_x_node" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_manhole"
-- ----------------------------
ALTER TABLE "man_manhole" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "man_type_event"
-- ----------------------------
ALTER TABLE "man_type_event" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_type_node"
-- ----------------------------
ALTER TABLE "man_type_node" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_arccategory"
-- ----------------------------
ALTER TABLE "man_value_arccategory" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_arcloc"
-- ----------------------------
ALTER TABLE "man_value_arcloc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_conssta"
-- ----------------------------
ALTER TABLE "man_value_conssta" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_coversta"
-- ----------------------------
ALTER TABLE "man_value_coversta" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_direction"
-- ----------------------------
ALTER TABLE "man_value_direction" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_elemtype"
-- ----------------------------
ALTER TABLE "man_value_elemtype" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_gratesta"
-- ----------------------------
ALTER TABLE "man_value_gratesta" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_roadloc"
-- ----------------------------
ALTER TABLE "man_value_roadloc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_sedsta"
-- ----------------------------
ALTER TABLE "man_value_sedsta" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_soildata"
-- ----------------------------
ALTER TABLE "man_value_soildata" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_value_systemtype"
-- ----------------------------
ALTER TABLE "man_value_systemtype" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "man_workstage"
-- ----------------------------
ALTER TABLE "man_workstage" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table "node"
-- ----------------------------
ALTER TABLE "node" ADD PRIMARY KEY ("node_id");

-- ----------------------------
-- Primary Key structure for table "raingage"
-- ----------------------------
ALTER TABLE "raingage" ADD PRIMARY KEY ("rg_id");

-- ----------------------------
-- Primary Key structure for table "result_selection"
-- ----------------------------
ALTER TABLE "result_selection" ADD PRIMARY KEY ("result_id");

-- ----------------------------
-- Primary Key structure for table "rpt_arcflow_sum"
-- ----------------------------
ALTER TABLE "rpt_arcflow_sum" ADD PRIMARY KEY ("result_id", "arc_id");

-- ----------------------------
-- Primary Key structure for table "rpt_condsurcharge_sum"
-- ----------------------------
ALTER TABLE "rpt_condsurcharge_sum" ADD PRIMARY KEY ("result_id", "arc_id");

-- ----------------------------
-- Primary Key structure for table "rpt_continuity_errors"
-- ----------------------------
ALTER TABLE "rpt_continuity_errors" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "rpt_critical_elements"
-- ----------------------------
ALTER TABLE "rpt_critical_elements" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "rpt_flowclass_sum"
-- ----------------------------
ALTER TABLE "rpt_flowclass_sum" ADD PRIMARY KEY ("result_id", "arc_id");

-- ----------------------------
-- Primary Key structure for table "rpt_flowrouting_cont"
-- ----------------------------
ALTER TABLE "rpt_flowrouting_cont" ADD PRIMARY KEY ("result_id");

-- ----------------------------
-- Primary Key structure for table "rpt_groundwater_cont"
-- ----------------------------
ALTER TABLE "rpt_groundwater_cont" ADD PRIMARY KEY ("result_id");

-- ----------------------------
-- Primary Key structure for table "rpt_high_conterrors"
-- ----------------------------
ALTER TABLE "rpt_high_conterrors" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "rpt_high_flowinest_ind"
-- ----------------------------
ALTER TABLE "rpt_high_flowinest_ind" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "rpt_instability_index"
-- ----------------------------
ALTER TABLE "rpt_instability_index" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "rpt_lidperformance_sum"
-- ----------------------------
ALTER TABLE "rpt_lidperformance_sum" ADD PRIMARY KEY ("result_id", "subc_id", "lidco_id");

-- ----------------------------
-- Primary Key structure for table "rpt_nodedepth_sum"
-- ----------------------------
ALTER TABLE "rpt_nodedepth_sum" ADD PRIMARY KEY ("result_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "rpt_nodeflooding_sum"
-- ----------------------------
ALTER TABLE "rpt_nodeflooding_sum" ADD PRIMARY KEY ("result_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "rpt_nodeinflow_sum"
-- ----------------------------
ALTER TABLE "rpt_nodeinflow_sum" ADD PRIMARY KEY ("result_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "rpt_nodesurcharge_sum"
-- ----------------------------
ALTER TABLE "rpt_nodesurcharge_sum" ADD PRIMARY KEY ("result_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "rpt_outfallflow_sum"
-- ----------------------------
ALTER TABLE "rpt_outfallflow_sum" ADD PRIMARY KEY ("result_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "rpt_outfallload_sum"
-- ----------------------------
ALTER TABLE "rpt_outfallload_sum" ADD PRIMARY KEY ("result_id", "poll_id", "node_id");

-- ----------------------------
-- Primary Key structure for table "rpt_pumping_sum"
-- ----------------------------
ALTER TABLE "rpt_pumping_sum" ADD PRIMARY KEY ("result_id", "pump_id");

-- ----------------------------
-- Primary Key structure for table "rpt_qualrouting_cont"
-- ----------------------------
ALTER TABLE "rpt_qualrouting_cont" ADD PRIMARY KEY ("result_id", "poll_id");

-- ----------------------------
-- Primary Key structure for table "rpt_rainfall_dep"
-- ----------------------------
ALTER TABLE "rpt_rainfall_dep" ADD PRIMARY KEY ("result_id");

-- ----------------------------
-- Primary Key structure for table "rpt_result_cat"
-- ----------------------------
ALTER TABLE "rpt_result_cat" ADD PRIMARY KEY ("result_id");

-- ----------------------------
-- Primary Key structure for table "rpt_routing_timestep"
-- ----------------------------
ALTER TABLE "rpt_routing_timestep" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "rpt_runoff_qual"
-- ----------------------------
ALTER TABLE "rpt_runoff_qual" ADD PRIMARY KEY ("result_id", "poll_id");

-- ----------------------------
-- Primary Key structure for table "rpt_runoff_quant"
-- ----------------------------
ALTER TABLE "rpt_runoff_quant" ADD PRIMARY KEY ("result_id");

-- ----------------------------
-- Primary Key structure for table "rpt_storagevol_sum"
-- ----------------------------
ALTER TABLE "rpt_storagevol_sum" ADD PRIMARY KEY ("result_id", "stor_id");

-- ----------------------------
-- Primary Key structure for table "rpt_subcatchwashoff_sum"
-- ----------------------------
ALTER TABLE "rpt_subcatchwashoff_sum" ADD PRIMARY KEY ("result_id", "subc_id", "poll_id");

-- ----------------------------
-- Primary Key structure for table "rpt_subcathrunoff_sum"
-- ----------------------------
ALTER TABLE "rpt_subcathrunoff_sum" ADD PRIMARY KEY ("result_id", "subc_id");

-- ----------------------------
-- Primary Key structure for table "rpt_timestep_critelem"
-- ----------------------------
ALTER TABLE "rpt_timestep_critelem" ADD PRIMARY KEY ("result_id", "text");

-- ----------------------------
-- Primary Key structure for table "subcatchment"
-- ----------------------------
ALTER TABLE "subcatchment" ADD PRIMARY KEY ("subc_id");

-- ----------------------------
-- Primary Key structure for table "vertice"
-- ----------------------------
ALTER TABLE "vertice" ADD PRIMARY KEY ("vertice_id");

-- ----------------------------
-- Foreign Key structure for table "arc"
-- ----------------------------
ALTER TABLE "arc" ADD FOREIGN KEY ("catch_id") REFERENCES "catchment" ("catch_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "arc" ADD FOREIGN KEY ("matcat_id") REFERENCES "cat_mat" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "arc" ADD FOREIGN KEY ("arccat_id") REFERENCES "cat_arc" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "catch_selection"
-- ----------------------------
ALTER TABLE "catch_selection" ADD FOREIGN KEY ("catch_id") REFERENCES "catchment" ("catch_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_conduit"
-- ----------------------------
ALTER TABLE "inp_conduit" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_coverage_land_x_subc"
-- ----------------------------
ALTER TABLE "inp_coverage_land_x_subc" ADD FOREIGN KEY ("subc_id") REFERENCES "subcatchment" ("subc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_curve"
-- ----------------------------
ALTER TABLE "inp_curve" ADD FOREIGN KEY ("curve_id") REFERENCES "inp_curve_id" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_curve_id"
-- ----------------------------
ALTER TABLE "inp_curve_id" ADD FOREIGN KEY ("curve_type") REFERENCES "inp_value_curve" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_divider"
-- ----------------------------
ALTER TABLE "inp_divider" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_dwf"
-- ----------------------------
ALTER TABLE "inp_dwf" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_dwf_pol_x_node"
-- ----------------------------
ALTER TABLE "inp_dwf_pol_x_node" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_evaporation"
-- ----------------------------
ALTER TABLE "inp_evaporation" ADD FOREIGN KEY ("evap_type") REFERENCES "inp_typevalue_evap" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_evaporation" ADD FOREIGN KEY ("dry_only") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_inflows"
-- ----------------------------
ALTER TABLE "inp_inflows" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_inflows_pol_x_node"
-- ----------------------------
ALTER TABLE "inp_inflows_pol_x_node" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_inflows_pol_x_node" ADD FOREIGN KEY ("poll_id") REFERENCES "inp_pollutant" ("poll_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_junction"
-- ----------------------------
ALTER TABLE "inp_junction" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_loadings_pol_x_subc"
-- ----------------------------
ALTER TABLE "inp_loadings_pol_x_subc" ADD FOREIGN KEY ("subc_id") REFERENCES "subcatchment" ("subc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_options"
-- ----------------------------
ALTER TABLE "inp_options" ADD FOREIGN KEY ("link_offsets") REFERENCES "inp_value_options_lo" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("allow_ponding") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("force_main_equation") REFERENCES "inp_value_options_fme" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("flow_routing") REFERENCES "inp_value_options_fr" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("ignore_routing") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("flow_units") REFERENCES "inp_value_options_fu" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("inertial_damping") REFERENCES "inp_value_options_id" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("ignore_groundwater") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("infiltration") REFERENCES "inp_value_options_in" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("ignore_snowmelt") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("ignore_rainfall") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("normal_flow_limited") REFERENCES "inp_value_options_nfl" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("ignore_quality") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_options" ADD FOREIGN KEY ("skip_steady_state") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_orifice"
-- ----------------------------
ALTER TABLE "inp_orifice" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_outfall"
-- ----------------------------
ALTER TABLE "inp_outfall" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_outlet"
-- ----------------------------
ALTER TABLE "inp_outlet" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_pump"
-- ----------------------------
ALTER TABLE "inp_pump" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_rdii"
-- ----------------------------
ALTER TABLE "inp_rdii" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_report"
-- ----------------------------
ALTER TABLE "inp_report" ADD FOREIGN KEY ("input") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_report" ADD FOREIGN KEY ("controls") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_report" ADD FOREIGN KEY ("continuity") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_report" ADD FOREIGN KEY ("subcatchments") REFERENCES "inp_value_allnone" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_report" ADD FOREIGN KEY ("flowstats") REFERENCES "inp_value_yesno" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_report" ADD FOREIGN KEY ("links") REFERENCES "inp_value_allnone" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_report" ADD FOREIGN KEY ("nodes") REFERENCES "inp_value_allnone" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_storage"
-- ----------------------------
ALTER TABLE "inp_storage" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_temperature"
-- ----------------------------
ALTER TABLE "inp_temperature" ADD FOREIGN KEY ("temp_type") REFERENCES "inp_typevalue_temp" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_timeseries"
-- ----------------------------
ALTER TABLE "inp_timeseries" ADD FOREIGN KEY ("timser_id") REFERENCES "inp_timser_id" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_timser_id"
-- ----------------------------
ALTER TABLE "inp_timser_id" ADD FOREIGN KEY ("times_type") REFERENCES "inp_value_timserid" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_treatment_node_x_pol"
-- ----------------------------
ALTER TABLE "inp_treatment_node_x_pol" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_weir"
-- ----------------------------
ALTER TABLE "inp_weir" ADD FOREIGN KEY ("weir_type") REFERENCES "inp_value_weirs" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "inp_weir" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "inp_windspeed"
-- ----------------------------
ALTER TABLE "inp_windspeed" ADD FOREIGN KEY ("wind_type") REFERENCES "inp_typevalue_windsp" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "man_arcdat"
-- ----------------------------
ALTER TABLE "man_arcdat" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "man_event_x_arc"
-- ----------------------------
ALTER TABLE "man_event_x_arc" ADD FOREIGN KEY ("event_id") REFERENCES "man_event" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "man_event_x_arc" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "man_event_x_node"
-- ----------------------------
ALTER TABLE "man_event_x_node" ADD FOREIGN KEY ("event_id") REFERENCES "man_event" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "man_event_x_node" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "man_manhole"
-- ----------------------------
ALTER TABLE "man_manhole" ADD FOREIGN KEY ("covcat_id") REFERENCES "cat_cover" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "man_manhole" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "raingage"
-- ----------------------------
ALTER TABLE "raingage" ADD FOREIGN KEY ("form_type") REFERENCES "inp_value_raingage" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "raingage" ADD FOREIGN KEY ("timser_id") REFERENCES "inp_timser_id" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "raingage" ADD FOREIGN KEY ("rgage_type") REFERENCES "inp_typevalue_raingage" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "result_selection"
-- ----------------------------
ALTER TABLE "result_selection" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_arcflow_sum"
-- ----------------------------
ALTER TABLE "rpt_arcflow_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_arcflow_sum" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_condsurcharge_sum"
-- ----------------------------
ALTER TABLE "rpt_condsurcharge_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_continuity_errors"
-- ----------------------------
ALTER TABLE "rpt_continuity_errors" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_critical_elements"
-- ----------------------------
ALTER TABLE "rpt_critical_elements" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_flowclass_sum"
-- ----------------------------
ALTER TABLE "rpt_flowclass_sum" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_flowclass_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_flowrouting_cont"
-- ----------------------------
ALTER TABLE "rpt_flowrouting_cont" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_groundwater_cont"
-- ----------------------------
ALTER TABLE "rpt_groundwater_cont" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_high_conterrors"
-- ----------------------------
ALTER TABLE "rpt_high_conterrors" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_high_flowinest_ind"
-- ----------------------------
ALTER TABLE "rpt_high_flowinest_ind" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_instability_index"
-- ----------------------------
ALTER TABLE "rpt_instability_index" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_lidperformance_sum"
-- ----------------------------
ALTER TABLE "rpt_lidperformance_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_nodedepth_sum"
-- ----------------------------
ALTER TABLE "rpt_nodedepth_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_nodeflooding_sum"
-- ----------------------------
ALTER TABLE "rpt_nodeflooding_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_nodeflooding_sum" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_nodeinflow_sum"
-- ----------------------------
ALTER TABLE "rpt_nodeinflow_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_nodeinflow_sum" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_nodesurcharge_sum"
-- ----------------------------
ALTER TABLE "rpt_nodesurcharge_sum" ADD FOREIGN KEY ("node_id") REFERENCES "node" ("node_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_nodesurcharge_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_outfallflow_sum"
-- ----------------------------
ALTER TABLE "rpt_outfallflow_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_outfallload_sum"
-- ----------------------------
ALTER TABLE "rpt_outfallload_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_pumping_sum"
-- ----------------------------
ALTER TABLE "rpt_pumping_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_qualrouting_cont"
-- ----------------------------
ALTER TABLE "rpt_qualrouting_cont" ADD FOREIGN KEY ("poll_id") REFERENCES "inp_pollutant" ("poll_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_qualrouting_cont" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_rainfall_dep"
-- ----------------------------
ALTER TABLE "rpt_rainfall_dep" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_routing_timestep"
-- ----------------------------
ALTER TABLE "rpt_routing_timestep" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_runoff_qual"
-- ----------------------------
ALTER TABLE "rpt_runoff_qual" ADD FOREIGN KEY ("poll_id") REFERENCES "inp_pollutant" ("poll_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_runoff_qual" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_runoff_quant"
-- ----------------------------
ALTER TABLE "rpt_runoff_quant" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_storagevol_sum"
-- ----------------------------
ALTER TABLE "rpt_storagevol_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_subcatchwashoff_sum"
-- ----------------------------
ALTER TABLE "rpt_subcatchwashoff_sum" ADD FOREIGN KEY ("subc_id") REFERENCES "subcatchment" ("subc_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_subcatchwashoff_sum" ADD FOREIGN KEY ("poll_id") REFERENCES "inp_pollutant" ("poll_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_subcatchwashoff_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_subcathrunoff_sum"
-- ----------------------------
ALTER TABLE "rpt_subcathrunoff_sum" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "rpt_subcathrunoff_sum" ADD FOREIGN KEY ("subc_id") REFERENCES "subcatchment" ("subc_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "rpt_timestep_critelem"
-- ----------------------------
ALTER TABLE "rpt_timestep_critelem" ADD FOREIGN KEY ("result_id") REFERENCES "rpt_result_cat" ("result_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "subcatchment"
-- ----------------------------
ALTER TABLE "subcatchment" ADD FOREIGN KEY ("catch_id") REFERENCES "catchment" ("catch_id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "vertice"
-- ----------------------------
ALTER TABLE "vertice" ADD FOREIGN KEY ("arc_id") REFERENCES "arc" ("arc_id") ON DELETE RESTRICT ON UPDATE CASCADE;
