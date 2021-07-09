# Based on NonTrival-Mips project(https://github.com/trivialmips/nontrivial-mips)

update_compile_order -fileset sources_1

# Check jobs number
if {[info exists env(JOBS_NUMBER)]} {
    set jobs_number $env(JOBS_NUMBER)
    puts "JOBS NUMBER is $jobs_number"
} else {
    set jobs_number 2
}


# Add our rtl
add_files -norecurse ./mycpu_top.v
add_files -norecurse ./mergeRTL.v

# Add our ips
set ip_paths [glob ./rtl/*/*.xci]
foreach ip $ip_paths {
    add_files -norecurse $ip
}

# change coe files
if {[info exists env(AXI_RAM_COE_FILE)]} {
    puts "COE is $env(AXI_RAM_COE_FILE)"
    # remove other coe_files
    remove_files inst_ram.coe
    remove_files axi_ram.coe
    # add new coe_files
    add_files -norecurse "[pwd]/$env(AXI_RAM_COE_FILE)"

    set_property -dict [list CONFIG.Coe_File "[pwd]/$env(AXI_RAM_COE_FILE)"] [get_ips axi_ram]
    set axi_ram_ip_dir [get_property IP_DIR [get_ips axi_ram]]
    generate_target all [get_files $axi_ram_ip_dir/axi_ram.xci]
    export_ip_user_files -of_objects [get_files $axi_ram_ip_dir/axi_ram.xci] -no_script -sync -force -quiet
}


update_compile_order -fileset sources_1

# If IP cores are used
if { [llength [get_ips]] != 0} {
    upgrade_ip [get_ips]

    foreach ip [get_ips] {
        create_ip_run [get_ips $ip]
    }

    set ip_runs [get_runs -filter {SRCSET != sources_1 && IS_SYNTHESIS && STATUS != "synth_design Complete!"}]
    
    if { [llength $ip_runs] != 0} {
        launch_runs -quiet -jobs $jobs_number {*}$ip_runs
        
        foreach r $ip_runs {
            wait_on_run $r
        }
    }

}

exit